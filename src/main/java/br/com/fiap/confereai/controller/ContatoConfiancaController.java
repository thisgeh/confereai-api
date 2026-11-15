package br.com.fiap.confereai.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.fiap.confereai.dto.ContatoConfiancaRequest;
import br.com.fiap.confereai.dto.ContatoConfiancaResponse;
import br.com.fiap.confereai.dto.ErrorResponse;
import br.com.fiap.confereai.service.ContatoConfiancaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/contatos")
public class ContatoConfiancaController {

    private final ContatoConfiancaService service;

    public ContatoConfiancaController(ContatoConfiancaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar contato de confiança",
            description = "O primeiro contato do usuário vira automaticamente o principal.")
    @ApiResponse(responseCode = "201", description = "Contato criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ContatoConfiancaResponse> create(@Valid @RequestBody ContatoConfiancaRequest request) {
        ContatoConfiancaResponse criado = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar contatos (opcionalmente de um usuário)")
    public ResponseEntity<List<ContatoConfiancaResponse>> findAll(
            @Parameter(description = "Filtrar pelos contatos deste usuário") @RequestParam(required = false) Long usuarioId) {
        return ResponseEntity.ok(service.findAll(usuarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar contato por id")
    @ApiResponse(responseCode = "200", description = "Contato encontrado")
    @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ContatoConfiancaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar contato")
    @ApiResponse(responseCode = "200", description = "Contato atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Contato ou usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ContatoConfiancaResponse> update(@PathVariable Long id,
            @Valid @RequestBody ContatoConfiancaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir contato")
    @ApiResponse(responseCode = "204", description = "Contato excluído")
    @ApiResponse(responseCode = "404", description = "Contato não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
