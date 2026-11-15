package br.com.fiap.confereai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI confereAiOpenApi() {
        return new OpenAPI().info(new Info()
                .title("ConfereAí API")
                .version("v1")
                .description("""
                        API do **ConfereAí** — Global Solution 2026 (FIAP).

                        Ajuda pessoas idosas a criar o hábito de **conferir antes de pagar** um boleto ou Pix:
                        1. Confere quem está cobrando (CNPJ na Receita Federal via BrasilAPI);
                        2. Confere o banco do boleto (3 primeiros números na lista do Banco Central);
                        3. Faz o teste do golpe (perguntas Sim/Não);
                        4. Devolve um semáforo 🟢🟡🔴 com explicações simples e o contato de confiança para ligar.
                        """)
                .contact(new Contact().name("Equipe ConfereAí")));
    }
}
