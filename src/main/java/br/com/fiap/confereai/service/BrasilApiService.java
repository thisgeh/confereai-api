package br.com.fiap.confereai.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import br.com.fiap.confereai.dto.BrasilApiBanco;
import br.com.fiap.confereai.dto.BrasilApiCnpj;
import br.com.fiap.confereai.exception.ExternalServiceException;

/**
 * Integração com a BrasilAPI (https://brasilapi.com.br):
 */
@Service
public class BrasilApiService {

    private final RestClient restClient;

    public BrasilApiService(@Value("${brasilapi.url}") String baseUrl,
            @Value("${brasilapi.timeout-segundos:8}") long timeoutSegundos) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(timeoutSegundos));
        factory.setReadTimeout(Duration.ofSeconds(timeoutSegundos));
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .defaultHeader("User-Agent", "ConfereAi/1.0 (FIAP Global Solution)")
                .defaultHeader("Accept", "application/json")
                .build();
    }


    public Optional<BrasilApiCnpj> buscarCnpj(String cnpj) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/cnpj/v1/{cnpj}", cnpj)
                    .retrieve()
                    .body(BrasilApiCnpj.class));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 404 || ex.getStatusCode().value() == 400) {
                return Optional.empty();
            }
            throw new ExternalServiceException("Não foi possível consultar a Receita Federal agora (BrasilAPI).", ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Não foi possível consultar a Receita Federal agora (BrasilAPI).", ex);
        }
    }


    public Optional<BrasilApiBanco> buscarBanco(String codigo) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/banks/v1/{codigo}", Integer.parseInt(codigo))
                    .retrieve()
                    .body(BrasilApiBanco.class));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 404 || ex.getStatusCode().value() == 400) {
                return Optional.empty();
            }
            throw new ExternalServiceException("Não foi possível consultar a lista de bancos agora (BrasilAPI).", ex);
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Não foi possível consultar a lista de bancos agora (BrasilAPI).", ex);
        }
    }
}
