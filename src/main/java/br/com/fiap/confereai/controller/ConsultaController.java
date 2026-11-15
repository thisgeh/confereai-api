package br.com.fiap.confereai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.confereai.dto.BancoConsultaResponse;
import br.com.fiap.confereai.dto.CnpjConsultaResponse;
import br.com.fiap.confereai.dto.ErrorResponse;
import br.com.fiap.confereai.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/${api.version}/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping("/cnpj/{cnpj}")
    @Operation(summary = "Conferir CNPJ", description = "Informe os 14 caracteres sem pontuação. Ex.: 00000000000191 (ou alfanumérico 12ABC34501DE35)")
    @ApiResponse(responseCode = "200", description = "Resultado da consulta (encontrada=false se não existir)")
    @ApiResponse(responseCode = "400", description = "CNPJ com formato inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "503", description = "BrasilAPI indisponível", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CnpjConsultaResponse> consultarCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(service.consultarCnpj(cnpj));
    }

    @GetMapping("/bancos/{codigo}")
    @Operation(summary = "Conferir banco do boleto",
            description = "Informe o código do banco (ex.: 001) ou a linha digitável do boleto só com números; usamos os 3 primeiros dígitos.")
    @ApiResponse(responseCode = "200", description = "Resultado da consulta (encontrado=false se não existir)")
    @ApiResponse(responseCode = "400", description = "Código inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "503", description = "BrasilAPI indisponível", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<BancoConsultaResponse> consultarBanco(@PathVariable String codigo) {
        return ResponseEntity.ok(service.consultarBanco(codigo));
    }
}
