package br.com.fiap.confereai.dto;

import java.time.LocalDate;


public record CnpjConsultaResponse(
        String cnpj,
        boolean encontrada,
        String razaoSocial,
        String nomeFantasia,
        String situacaoCadastral,
        boolean ativa,
        LocalDate dataAbertura,
        Integer anosDeAbertura,
        String mensagem) {
}
