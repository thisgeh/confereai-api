package br.com.fiap.confereai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TextoUtilsTest {

    @Test
    void deveValidarDigitosDoCnpj() {
        assertTrue(TextoUtils.cnpjValido("00.000.000/0001-91"));
        assertTrue(TextoUtils.cnpjValido("60701190000104"));
        assertFalse(TextoUtils.cnpjValido("00000000000192"));
        assertFalse(TextoUtils.cnpjValido("11111111111111"));
        assertFalse(TextoUtils.cnpjValido("123"));
        assertTrue(TextoUtils.cnpjValido("12.ABC.345/01DE-35"));
        assertFalse(TextoUtils.cnpjValido("12.ABC.345/01DE-36"));
    }

    @Test
    void deveExtrairSomenteDigitos() {
        assertEquals("00000000000191", TextoUtils.somenteDigitos("00.000.000/0001-91"));
        assertEquals("", TextoUtils.somenteDigitos(null));
    }

    @Test
    void deveCompararNomeInformadoComNomeOficial() {
        assertTrue(TextoUtils.nomeConfere("Banco do Brasil", "BANCO DO BRASIL SA", null));
        assertTrue(TextoUtils.nomeConfere("itaú", "ITAU UNIBANCO S.A.", null));
        assertTrue(TextoUtils.nomeConfere("Nubank", "NU PAGAMENTOS S.A.", "NUBANK"));
        assertFalse(TextoUtils.nomeConfere("Banco Itaú", "BANCO DO BRASIL SA", null));
        assertFalse(TextoUtils.nomeConfere("Casas Bahia", "BANCO DO BRASIL SA", null));
    }
}
