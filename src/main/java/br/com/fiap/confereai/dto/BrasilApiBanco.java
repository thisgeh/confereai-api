package br.com.fiap.confereai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public record BrasilApiBanco(
        @JsonProperty("ispb") String ispb,
        @JsonProperty("name") String nome,
        @JsonProperty("code") Integer codigo,
        @JsonProperty("fullName") String nomeCompleto) {
}
