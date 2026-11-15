package br.com.fiap.confereai.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar/atualizar um usuário (idoso)")
public class UsuarioRequest {

    @Schema(example = "Maria Aparecida da Silva")
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    private String nome;

    @Schema(example = "maria.silva@email.com")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 150)
    private String email;

    @Schema(example = "11987654321")
    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "^\\d{10,13}$", message = "Telefone deve ter de 10 a 13 dígitos, apenas números (DDD + número)")
    private String telefone;

    @Schema(example = "1952-03-15")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}
