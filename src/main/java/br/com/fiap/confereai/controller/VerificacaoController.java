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

import br.com.fiap.confereai.dto.ErrorResponse;
import br.com.fiap.confereai.dto.VerificacaoRequest;
import br.com.fiap.confereai.dto.VerificacaoResponse;
import br.com.fiap.confereai.service.VerificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/verificacoes")
public class VerificacaoController {

    private final VerificacaoService service;

    public VerificacaoController(VerificacaoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Fazer uma nova verificação",
            description = "Consulta o CNPJ na Receita Federal e o banco do boleto no Banco Central (via BrasilAPI), "
                    + "soma os sinais de alerta das respostas SIM e devolve o semáforo com explicações simples.")
    @ApiResponse(responseCode = "201", description = "Verificação realizada e salva")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<VerificacaoResponse> create(@Valid @RequestBody VerificacaoRequest request) {
        VerificacaoResponse criada = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar histórico")
    public ResponseEntity<List<VerificacaoResponse>> findAll(
            @Parameter(description = "Filtrar pelo histórico deste usuário") @RequestParam(required = false) Long usuarioId) {
        return ResponseEntity.ok(service.findAll(usuarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar verificação por id")
    @ApiResponse(responseCode = "200", description = "Verificação encontrada")
    @ApiResponse(responseCode = "404", description = "Verificação não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<VerificacaoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar verificação")
    @ApiResponse(responseCode = "200", description = "Verificação atualizada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Verificação ou usuário não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<VerificacaoResponse> update(@PathVariable Long id, @Valid @RequestBody VerificacaoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir verificação")
    @ApiResponse(responseCode = "204", description = "Verificação excluída")
    @ApiResponse(responseCode = "404", description = "Verificação não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
