package br.com.fiap.confereai.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.confereai.model.TipoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados que o idoso informa antes de pagar. A API consulta a BrasilAPI e devolve o semáforo.")
public class VerificacaoRequest {

    @Schema(example = "1")
    @NotNull(message = "O usuarioId é obrigatório")
    @Positive(message = "O usuarioId deve ser positivo")
    private Long usuarioId;

    @Schema(example = "BOLETO")
    @NotNull(message = "O tipo de pagamento é obrigatório (PIX ou BOLETO)")
    private TipoPagamento tipoPagamento;

    @Schema(description = "CNPJ de quem está cobrando, numérico ou alfanumérico, com ou sem pontuação", example = "00.000.000/0001-91")
    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(regexp = "^[A-Za-z0-9]{2}\\.?[A-Za-z0-9]{3}\\.?[A-Za-z0-9]{3}/?[A-Za-z0-9]{4}-?\\d{2}$",
            message = "CNPJ deve ter 14 caracteres (ex.: 00.000.000/0001-91 ou 12.ABC.345/01DE-35)")
    private String cnpj;

    @Schema(description = "Nome que disseram ser (para comparar com a Receita)", example = "Banco do Brasil")
    @Size(max = 150)
    private String nomeInformado;

    @Schema(description = "Linha digitável do boleto (os 3 primeiros números identificam o banco)",
            example = "00190.00009 01234.567890 12345.678901 1 23450000010000")
    @Size(max = 60)
    @Pattern(regexp = "^[\\d .-]*$", message = "A linha digitável deve conter apenas números, espaços e pontos")
    private String linhaDigitavel;

    @Schema(description = "Código do banco (3 dígitos). Opcional se a linha digitável for informada.", example = "001")
    @Pattern(regexp = "^\\d{3}$", message = "O código do banco deve ter 3 dígitos")
    private String codigoBanco;

    @Schema(example = "150.00")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @Schema(description = "IDs das perguntas do teste do golpe respondidas com SIM", example = "[]")
    private List<@NotNull @Positive Long> respostasSim = new ArrayList<>();

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getNomeInformado() { return nomeInformado; }
    public void setNomeInformado(String nomeInformado) { this.nomeInformado = nomeInformado; }
    public String getLinhaDigitavel() { return linhaDigitavel; }
    public void setLinhaDigitavel(String linhaDigitavel) { this.linhaDigitavel = linhaDigitavel; }
    public String getCodigoBanco() { return codigoBanco; }
    public void setCodigoBanco(String codigoBanco) { this.codigoBanco = codigoBanco; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public List<Long> getRespostasSim() { return respostasSim; }
    public void setRespostasSim(List<Long> respostasSim) { this.respostasSim = respostasSim; }
}
