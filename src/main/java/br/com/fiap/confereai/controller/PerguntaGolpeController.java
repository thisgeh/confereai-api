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
import br.com.fiap.confereai.dto.PerguntaGolpeRequest;
import br.com.fiap.confereai.dto.PerguntaGolpeResponse;
import br.com.fiap.confereai.service.PerguntaGolpeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/${api.version}/perguntas")
public class PerguntaGolpeController {

    private final PerguntaGolpeService service;

    public PerguntaGolpeController(PerguntaGolpeService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar pergunta")
    @ApiResponse(responseCode = "201", description = "Pergunta criada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PerguntaGolpeResponse> create(@Valid @RequestBody PerguntaGolpeRequest request) {
        PerguntaGolpeResponse criada = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar perguntas", description = "Use somenteAtivas=true para montar o teste no app.")
    public ResponseEntity<List<PerguntaGolpeResponse>> findAll(
            @Parameter(description = "Retornar apenas perguntas ativas") @RequestParam(defaultValue = "false") boolean somenteAtivas) {
        return ResponseEntity.ok(service.findAll(somenteAtivas));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pergunta por id")
    @ApiResponse(responseCode = "200", description = "Pergunta encontrada")
    @ApiResponse(responseCode = "404", description = "Pergunta não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PerguntaGolpeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pergunta")
    @ApiResponse(responseCode = "200", description = "Pergunta atualizada")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Pergunta não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PerguntaGolpeResponse> update(@PathVariable Long id, @Valid @RequestBody PerguntaGolpeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pergunta", description = "Perguntas já usadas em verificações não podem ser excluídas (409); desative-as.")
    @ApiResponse(responseCode = "204", description = "Pergunta excluída")
    @ApiResponse(responseCode = "404", description = "Pergunta não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Pergunta em uso", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
