package br.com.fiap.confereai.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.fiap.confereai.service.PerguntaGolpeService;


@Component
public class DataSeeder implements CommandLineRunner {

    private final PerguntaGolpeService perguntaService;

    public DataSeeder(PerguntaGolpeService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @Override
    public void run(String... args) {
        perguntaService.carregarPerguntasPadrao();
    }
}
