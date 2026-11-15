package br.com.fiap.confereai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar/atualizar uma pessoa de confiança")
public class ContatoConfiancaRequest {

    @Schema(example = "Carlos (filho)")
    @NotBlank(message = "O nome do contato é obrigatório")
    @Size(min = 2, max = 120, message = "O nome deve ter entre 2 e 120 caracteres")
    private String nome;

    @Schema(example = "11912345678")
    @NotBlank(message = "O telefone do contato é obrigatório")
    @Pattern(regexp = "^\\d{10,13}$", message = "Telefone deve ter de 10 a 13 dígitos, apenas números (DDD + número)")
    private String telefone;

    @Schema(example = "Filho")
    @Size(max = 50)
    private String parentesco;

    @Schema(description = "Se este é o contato que aparece no botão 'Ligar agora'", example = "true")
    private Boolean principal;

    @Schema(example = "1")
    @NotNull(message = "O usuarioId é obrigatório")
    @Positive(message = "O usuarioId deve ser positivo")
    private Long usuarioId;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
    public Boolean getPrincipal() { return principal; }
    public void setPrincipal(Boolean principal) { this.principal = principal; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
