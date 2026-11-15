package br.com.fiap.confereai.dto;


public record BancoConsultaResponse(
        String codigo,
        boolean encontrado,
        String nome,
        String nomeCompleto,
        String ispb,
        String mensagem) {
}
