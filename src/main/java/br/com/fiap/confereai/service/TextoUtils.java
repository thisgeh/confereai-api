package br.com.fiap.confereai.service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public final class TextoUtils {


    private static final Set<String> PALAVRAS_GENERICAS = Set.of(
            "LTDA", "SA", "S", "A", "ME", "EPP", "EIRELI", "MEI", "CIA", "COMPANHIA",
            "DE", "DA", "DO", "DAS", "DOS", "E", "EM", "O", "OS", "AS",
            "BANCO", "BANK", "EMPRESA", "GRUPO", "SERVICOS", "SERVICO", "PAGAMENTO", "PAGAMENTOS",
            "COMERCIO", "INSTITUICAO", "FINANCEIRA", "BRASILEIRA", "LOJA", "LOJAS");

    private TextoUtils() {
    }

    public static String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }


    public static String normalizarCnpj(String cnpj) {
        return cnpj == null ? "" : cnpj.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }


    public static boolean cnpjValido(String cnpj) {
        String c = normalizarCnpj(cnpj);
        if (!c.matches("[A-Z0-9]{12}\\d{2}") || c.chars().distinct().count() == 1) {
            return false;
        }
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return digito(c, pesos1) == c.charAt(12) - '0' && digito(c, pesos2) == c.charAt(13) - '0';
    }

    private static int digito(String c, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (c.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toUpperCase().replaceAll("[^A-Z0-9]+", " ").trim();
    }


    public static boolean nomeConfere(String nomeInformado, String... nomesOficiais) {
        String alvo = normalizar(String.join(" ", Arrays.stream(nomesOficiais)
                .filter(n -> n != null).toList()));
        String alvoSemEspaco = alvo.replace(" ", "");
        if (alvo.isBlank()) {
            return false;
        }
        List<String> tokens = Arrays.stream(normalizar(nomeInformado).split(" "))
                .filter(t -> t.length() > 1 && !PALAVRAS_GENERICAS.contains(t))
                .toList();
        if (tokens.isEmpty()) {
            String informado = normalizar(nomeInformado);
            return !informado.isBlank() && alvo.contains(informado);
        }
        return tokens.stream().allMatch(t -> alvo.contains(t) || alvoSemEspaco.contains(t));
    }
}
