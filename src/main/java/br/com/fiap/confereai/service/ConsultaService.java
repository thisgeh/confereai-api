package br.com.fiap.confereai.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

import br.com.fiap.confereai.dto.BancoConsultaResponse;
import br.com.fiap.confereai.dto.BrasilApiCnpj;
import br.com.fiap.confereai.dto.CnpjConsultaResponse;
import br.com.fiap.confereai.exception.BusinessException;


@Service
public class ConsultaService {

    private final BrasilApiService brasilApi;

    public ConsultaService(BrasilApiService brasilApi) {
        this.brasilApi = brasilApi;
    }

    public CnpjConsultaResponse consultarCnpj(String cnpjInformado) {
        String cnpj = TextoUtils.normalizarCnpj(cnpjInformado);
        if (cnpj.length() != 14) {
            throw BusinessException.badRequest("O CNPJ deve ter 14 caracteres (números, ou letras e números no CNPJ alfanumérico).");
        }
        if (!TextoUtils.cnpjValido(cnpj)) {
            return new CnpjConsultaResponse(cnpj, false, null, null, null, false, null, null,
                    "🔴 Esse CNPJ não existe: os números não batem. Desconfie!");
        }
        return brasilApi.buscarCnpj(cnpj)
                .map(dados -> montar(cnpj, dados))
                .orElseGet(() -> new CnpjConsultaResponse(cnpj, false, null, null, null, false, null, null,
                        "🔴 Não encontramos esse CNPJ na Receita Federal. Pode ser uma empresa inventada."));
    }

    private CnpjConsultaResponse montar(String cnpj, BrasilApiCnpj dados) {
        LocalDate abertura = VerificacaoService.parseData(dados.dataInicioAtividade());
        Integer anos = abertura != null ? Period.between(abertura, LocalDate.now()).getYears() : null;
        boolean ativa = "ATIVA".equalsIgnoreCase(dados.situacaoCadastral());
        String msg = ativa
                ? "🟢 Empresa encontrada na Receita Federal e ativa." + (anos != null ? " Aberta há " + anos + " ano(s)." : "")
                : "🔴 Empresa encontrada, mas a situação na Receita é: " + dados.situacaoCadastral() + ".";
        return new CnpjConsultaResponse(cnpj, true, dados.razaoSocial(), dados.nomeFantasia(),
                dados.situacaoCadastral(), ativa, abertura, anos, msg);
    }


    public BancoConsultaResponse consultarBanco(String codigoOuLinhaDigitavel) {
        String digitos = TextoUtils.somenteDigitos(codigoOuLinhaDigitavel);
        if (digitos.length() < 3) {
            throw BusinessException.badRequest("Informe o código do banco (3 dígitos) ou a linha digitável do boleto.");
        }
        String codigo = digitos.substring(0, 3);
        return brasilApi.buscarBanco(codigo)
                .map(b -> new BancoConsultaResponse(codigo, true, b.nome(), b.nomeCompleto(), b.ispb(),
                        "🟢 O código " + codigo + " é do banco " + b.nome() + ", que está na lista oficial do Banco Central."))
                .orElseGet(() -> new BancoConsultaResponse(codigo, false, null, null, null,
                        "🔴 O código " + codigo + " não é de nenhum banco da lista oficial do Banco Central. Desconfie!"));
    }
}
