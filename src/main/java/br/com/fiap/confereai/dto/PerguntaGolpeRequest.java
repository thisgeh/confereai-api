package br.com.fiap.confereai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar/atualizar uma pergunta do teste do golpe")
public class PerguntaGolpeRequest {

    @Schema(example = "Pediram algum código que chegou por SMS ou WhatsApp?")
    @NotBlank(message = "O texto da pergunta é obrigatório")
    @Size(min = 10, max = 255, message = "O texto deve ter entre 10 e 255 caracteres")
    private String texto;

    @Schema(example = "Bancos e empresas sérias nunca pedem códigos. Quem pede o código quer entrar na sua conta.")
    @NotBlank(message = "A explicação é obrigatória")
    @Size(min = 10, max = 500, message = "A explicação deve ter entre 10 e 500 caracteres")
    private String explicacao;

    @Schema(description = "Peso do sinal de alerta (1 = leve, 5 = gravíssimo)", example = "3")
    @NotNull(message = "O peso é obrigatório")
    @Min(value = 1, message = "O peso mínimo é 1")
    @Max(value = 5, message = "O peso máximo é 5")
    private Integer peso;

    @Schema(example = "CODIGO")
    @Size(max = 50)
    private String categoria;

    @Schema(example = "true")
    private Boolean ativa;

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getExplicacao() { return explicacao; }
    public void setExplicacao(String explicacao) { this.explicacao = explicacao; }
    public Integer getPeso() { return peso; }
    public void setPeso(Integer peso) { this.peso = peso; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
}
