package br.com.fiap.confereai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record BrasilApiCnpj(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("razao_social") String razaoSocial,
        @JsonProperty("nome_fantasia") String nomeFantasia,
        @JsonProperty("descricao_situacao_cadastral") String situacaoCadastral,
        @JsonProperty("data_inicio_atividade") String dataInicioAtividade) {
}
