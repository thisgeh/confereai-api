package br.com.fiap.confereai.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.confereai.dto.BrasilApiBanco;
import br.com.fiap.confereai.dto.BrasilApiCnpj;
import br.com.fiap.confereai.dto.VerificacaoMapper;
import br.com.fiap.confereai.dto.VerificacaoRequest;
import br.com.fiap.confereai.dto.VerificacaoResponse;
import br.com.fiap.confereai.exception.ResourceNotFoundException;
import br.com.fiap.confereai.exception.ExternalServiceException;
import br.com.fiap.confereai.model.PerguntaGolpe;
import br.com.fiap.confereai.model.Semaforo;
import br.com.fiap.confereai.model.Verificacao;
import br.com.fiap.confereai.repository.VerificacaoRepository;


@Service
public class VerificacaoService {

    static final int LIMITE_AMARELO = 2;
    static final int LIMITE_VERMELHO = 5;

    private final VerificacaoRepository repository;
    private final VerificacaoMapper mapper;
    private final UsuarioService usuarioService;
    private final PerguntaGolpeService perguntaService;
    private final ContatoConfiancaService contatoService;
    private final BrasilApiService brasilApi;

    public VerificacaoService(VerificacaoRepository repository, VerificacaoMapper mapper,
            UsuarioService usuarioService, PerguntaGolpeService perguntaService,
            ContatoConfiancaService contatoService, BrasilApiService brasilApi) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioService = usuarioService;
        this.perguntaService = perguntaService;
        this.contatoService = contatoService;
        this.brasilApi = brasilApi;
    }

    @Transactional
    public VerificacaoResponse create(VerificacaoRequest request) {
        Verificacao verificacao = mapper.toModel(request);
        preencherEAvaliar(verificacao, request);
        return toDto(repository.save(verificacao));
    }

    @Transactional(readOnly = true)
    public List<VerificacaoResponse> findAll(Long usuarioId) {
        List<Verificacao> lista = usuarioId == null
                ? repository.findAll()
                : repository.findByUsuarioIdOrderByDataVerificacaoDesc(usuarioService.buscarEntidade(usuarioId).getId());
        return lista.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public VerificacaoResponse findById(Long id) {
        return toDto(buscarEntidade(id));
    }

    @Transactional
    public VerificacaoResponse update(Long id, VerificacaoRequest request) {
        Verificacao verificacao = buscarEntidade(id);
        mapper.updateModel(request, verificacao);
        preencherEAvaliar(verificacao, request);
        return toDto(repository.save(verificacao));
    }

    @Transactional
    public void deleteById(Long id) {
        Verificacao verificacao = buscarEntidade(id);
        verificacao.getUsuario().getVerificacoes().remove(verificacao);
        repository.delete(verificacao);
    }


    private void preencherEAvaliar(Verificacao v, VerificacaoRequest request) {
        v.setUsuario(usuarioService.buscarEntidade(request.getUsuarioId()));
        v.setCnpj(TextoUtils.normalizarCnpj(request.getCnpj()));
        v.setCodigoBanco(resolverCodigoBanco(request));
        v.setSinaisMarcados(new LinkedHashSet<>(
                request.getRespostasSim() == null ? List.of() : perguntaService.buscarEntidades(request.getRespostasSim())));
        limparResultadoAnterior(v);

        List<String> alertas = new ArrayList<>();
        int pontos = 0;
        pontos += avaliarEmpresa(v, alertas);
        pontos += avaliarBanco(v, alertas);
        pontos += avaliarRespostas(v, alertas);

        Semaforo semaforo = pontos >= LIMITE_VERMELHO ? Semaforo.VERMELHO
                : pontos >= LIMITE_AMARELO ? Semaforo.AMARELO : Semaforo.VERDE;
        if (alertas.isEmpty()) {
            alertas.add("🟢 A empresa existe, está ativa e não encontramos sinais de golpe nas suas respostas.");
        }
        v.setPontuacaoRisco(pontos);
        v.setResultado(semaforo);
        v.setAlertas(String.join(VerificacaoMapper.SEPARADOR_ALERTAS, alertas));
        v.setDataVerificacao(LocalDateTime.now());
    }

    
    private int avaliarEmpresa(Verificacao v, List<String> alertas) {
        if (!TextoUtils.cnpjValido(v.getCnpj())) {
            alertas.add("🔴 Esse CNPJ não existe: os números não batem. Empresas de verdade têm CNPJ válido.");
            return 5;
        }
        Optional<BrasilApiCnpj> resultado;
        try {
            resultado = brasilApi.buscarCnpj(v.getCnpj());
        } catch (ExternalServiceException ex) {
            alertas.add("🟡 Não conseguimos consultar a Receita Federal agora. Espere e confira de novo antes de pagar.");
            return 2;
        }
        if (resultado.isEmpty()) {
            alertas.add("🔴 Não encontramos esse CNPJ na Receita Federal. Pode ser uma empresa inventada.");
            return 5;
        }

        BrasilApiCnpj empresa = resultado.get();
        v.setRazaoSocial(empresa.razaoSocial());
        v.setNomeFantasia(empresa.nomeFantasia());
        v.setSituacaoCadastral(empresa.situacaoCadastral());
        v.setDataAbertura(parseData(empresa.dataInicioAtividade()));

        int pontos = 0;
        if (!"ATIVA".equalsIgnoreCase(empresa.situacaoCadastral())) {
            alertas.add("🔴 Na Receita Federal essa empresa está como \"" + empresa.situacaoCadastral()
                    + "\". Empresa que não está ativa não deveria estar cobrando você.");
            pontos += 5;
        }
        if (v.getDataAbertura() != null) {
            if (v.getDataAbertura().isAfter(LocalDate.now().minusMonths(6))) {
                alertas.add("🔴 A empresa foi aberta há menos de 6 meses. Golpistas costumam abrir empresas novas.");
                pontos += 3;
            } else if (v.getDataAbertura().isAfter(LocalDate.now().minusYears(1))) {
                alertas.add("🟡 A empresa foi aberta há menos de 1 ano. Tenha cuidado redobrado.");
                pontos += 2;
            }
        }
        if (v.getNomeInformado() != null && !v.getNomeInformado().isBlank()) {
            boolean confere = TextoUtils.nomeConfere(v.getNomeInformado(), empresa.razaoSocial(), empresa.nomeFantasia());
            v.setNomeConfere(confere);
            if (!confere) {
                alertas.add("🔴 Disseram que era \"" + v.getNomeInformado() + "\", mas esse CNPJ é de \""
                        + nomeExibicao(empresa) + "\". O nome não bate!");
                pontos += 3;
            }
        }
        return pontos;
    }

    private int avaliarBanco(Verificacao v, List<String> alertas) {
        if (v.getCodigoBanco() == null) {
            return 0;
        }
        Optional<BrasilApiBanco> banco;
        try {
            banco = brasilApi.buscarBanco(v.getCodigoBanco());
        } catch (ExternalServiceException ex) {
            alertas.add("🟡 Não conseguimos consultar a lista de bancos agora. Confira de novo antes de pagar.");
            return 1;
        }
        if (banco.isEmpty()) {
            alertas.add("🔴 Os 3 primeiros números do boleto (" + v.getCodigoBanco()
                    + ") não são de nenhum banco da lista oficial do Banco Central.");
            return 5;
        }
        v.setBancoNome(banco.get().nome());
        return 0;
    }

    private int avaliarRespostas(Verificacao v, List<String> alertas) {
        int pontos = 0;
        for (PerguntaGolpe p : v.getSinaisMarcados()) {
            String icone = p.getPeso() >= 3 ? "🔴" : "🟡";
            alertas.add(icone + " " + p.getTexto() + " Você respondeu SIM. " + p.getExplicacao());
            pontos += p.getPeso();
        }
        return pontos;
    }

    private String resolverCodigoBanco(VerificacaoRequest request) {
        if (request.getCodigoBanco() != null && !request.getCodigoBanco().isBlank()) {
            return request.getCodigoBanco();
        }
        String linha = TextoUtils.somenteDigitos(request.getLinhaDigitavel());
        return linha.length() >= 3 ? linha.substring(0, 3) : null;
    }

    private void limparResultadoAnterior(Verificacao v) {
        v.setRazaoSocial(null);
        v.setNomeFantasia(null);
        v.setSituacaoCadastral(null);
        v.setDataAbertura(null);
        v.setNomeConfere(null);
        v.setBancoNome(null);
    }

    private String nomeExibicao(BrasilApiCnpj empresa) {
        return empresa.nomeFantasia() != null && !empresa.nomeFantasia().isBlank()
                ? empresa.razaoSocial() + " / " + empresa.nomeFantasia()
                : empresa.razaoSocial();
    }

    static LocalDate parseData(String data) {
        try {
            return data == null || data.isBlank() ? null : LocalDate.parse(data);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private VerificacaoResponse toDto(Verificacao v) {
        VerificacaoResponse dto = mapper.toDto(v);
        contatoService.buscarPrincipal(v.getUsuario().getId()).ifPresent(dto::setContatoConfianca);
        return dto;
    }

    private Verificacao buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Verificação", id));
    }
}
