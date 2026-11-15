package br.com.fiap.confereai.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.confereai.dto.PerguntaGolpeMapper;
import br.com.fiap.confereai.dto.PerguntaGolpeRequest;
import br.com.fiap.confereai.dto.PerguntaGolpeResponse;
import br.com.fiap.confereai.exception.BusinessException;
import br.com.fiap.confereai.exception.ResourceNotFoundException;
import br.com.fiap.confereai.model.PerguntaGolpe;
import br.com.fiap.confereai.repository.PerguntaGolpeRepository;
import br.com.fiap.confereai.repository.VerificacaoRepository;

@Service
public class PerguntaGolpeService {

    private static final Logger log = LoggerFactory.getLogger(PerguntaGolpeService.class);

    private final PerguntaGolpeRepository repository;
    private final VerificacaoRepository verificacaoRepository;
    private final PerguntaGolpeMapper mapper;

    public PerguntaGolpeService(PerguntaGolpeRepository repository, VerificacaoRepository verificacaoRepository,
            PerguntaGolpeMapper mapper) {
        this.repository = repository;
        this.verificacaoRepository = verificacaoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PerguntaGolpeResponse create(PerguntaGolpeRequest request) {
        PerguntaGolpe pergunta = mapper.toModel(request);
        pergunta.setAtiva(request.getAtiva() == null || request.getAtiva());
        return mapper.toDto(repository.save(pergunta));
    }

    @Transactional(readOnly = true)
    public List<PerguntaGolpeResponse> findAll(boolean somenteAtivas) {
        List<PerguntaGolpe> perguntas = somenteAtivas ? repository.findByAtivaTrueOrderByIdAsc() : repository.findAll();
        return perguntas.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PerguntaGolpeResponse findById(Long id) {
        return mapper.toDto(buscarEntidade(id));
    }

    @Transactional
    public PerguntaGolpeResponse update(Long id, PerguntaGolpeRequest request) {
        PerguntaGolpe pergunta = buscarEntidade(id);
        mapper.updateModel(request, pergunta);
        pergunta.setAtiva(request.getAtiva() == null || request.getAtiva());
        return mapper.toDto(repository.save(pergunta));
    }

    @Transactional
    public void deleteById(Long id) {
        PerguntaGolpe pergunta = buscarEntidade(id);
        if (verificacaoRepository.existsBySinaisMarcadosId(id)) {
            throw BusinessException.conflict(
                    "Esta pergunta já foi usada em verificações e não pode ser excluída. Atualize-a com ativa=false.");
        }
        repository.delete(pergunta);
    }

    @Transactional(readOnly = true)
    public List<PerguntaGolpe> buscarEntidades(List<Long> ids) {
        List<Long> distintos = ids.stream().distinct().toList();
        List<PerguntaGolpe> encontradas = repository.findAllById(distintos);
        if (encontradas.size() != distintos.size()) {
            List<Long> achados = encontradas.stream().map(PerguntaGolpe::getId).toList();
            List<Long> faltando = distintos.stream().filter(i -> !achados.contains(i)).toList();
            throw BusinessException.badRequest("Pergunta(s) inexistente(s) em respostasSim: " + faltando);
        }
        return encontradas;
    }

    private PerguntaGolpe buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pergunta", id));
    }

    @Transactional
    public void carregarPerguntasPadrao() {
        if (repository.count() > 0) {
            return;
        }
        salvar("Pediram algum código que chegou por SMS ou WhatsApp?",
                "Bancos e empresas sérias nunca pedem códigos. Quem pede o código quer entrar na sua conta ou no seu WhatsApp.",
                5, "CODIGO");
        salvar("Disseram que é urgente ou que você vai perder algo se não pagar agora?",
                "Golpistas usam a pressa e o medo para você não ter tempo de pensar. Empresas de verdade dão prazo.",
                2, "URGENCIA");
        salvar("É um parente ou amigo falando de um número novo?",
                "O golpe do 'número novo' é muito comum. Ligue para o número antigo da pessoa antes de qualquer coisa.",
                3, "PARENTE");
        salvar("Pediram para fazer o Pix para uma pessoa (CPF), e não para a empresa?",
                "Empresas recebem em contas no nome delas. Pix para pessoa física em nome de empresa é sinal de golpe.",
                3, "PIX");
        salvar("Pediram para não contar a ninguém ou para não desligar o telefone?",
                "Ninguém sério pede segredo. Isso é feito para você não pedir ajuda a quem pode perceber o golpe.",
                3, "ISOLAMENTO");
        salvar("A oferta, o prêmio ou o desconto parece bom demais para ser verdade?",
                "Prêmios que você não pediu e descontos enormes costumam ser iscas para roubar dinheiro ou dados.",
                2, "OFERTA");
        salvar("Pediram para instalar um aplicativo ou clicar em um link?",
                "Links e aplicativos enviados por estranhos podem roubar seus dados ou controlar seu celular.",
                2, "LINK");
        log.info("Perguntas padrão do teste do golpe carregadas.");
    }

    private void salvar(String texto, String explicacao, int peso, String categoria) {
        PerguntaGolpe p = new PerguntaGolpe();
        p.setTexto(texto);
        p.setExplicacao(explicacao);
        p.setPeso(peso);
        p.setCategoria(categoria);
        p.setAtiva(true);
        repository.save(p);
    }
}
