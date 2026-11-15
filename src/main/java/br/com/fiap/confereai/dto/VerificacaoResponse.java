package br.com.fiap.confereai.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.confereai.model.Semaforo;
import br.com.fiap.confereai.model.TipoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;

public class VerificacaoResponse {
    private Long id;
    private Long usuarioId;
    private TipoPagamento tipoPagamento;
    private String cnpj;
    private String nomeInformado;
    private String codigoBanco;
    private BigDecimal valor;

    private String razaoSocial;
    private String nomeFantasia;
    private String situacaoCadastral;
    private LocalDate dataAbertura;
    private Boolean nomeConfere;
    private String bancoNome;

    private Integer pontuacaoRisco;

    @Schema(description = "Semáforo: VERDE, AMARELO ou VERMELHO")
    private Semaforo resultado;

    @Schema(description = "Mensagem principal em linguagem simples")
    private String mensagem;

    @Schema(description = "Explicação de cada sinal de alerta em linguagem simples")
    private List<String> alertas = new ArrayList<>();

    @Schema(description = "IDs das perguntas respondidas com SIM")
    private List<Long> respostasSim = new ArrayList<>();

    @Schema(description = "Pessoa de confiança para ligar antes de decidir (contato principal do usuário)")
    private ContatoConfiancaResponse contatoConfianca;

    private LocalDateTime dataVerificacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public TipoPagamento getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamento tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getNomeInformado() { return nomeInformado; }
    public void setNomeInformado(String nomeInformado) { this.nomeInformado = nomeInformado; }
    public String getCodigoBanco() { return codigoBanco; }
    public void setCodigoBanco(String codigoBanco) { this.codigoBanco = codigoBanco; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getSituacaoCadastral() { return situacaoCadastral; }
    public void setSituacaoCadastral(String situacaoCadastral) { this.situacaoCadastral = situacaoCadastral; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
    public Boolean getNomeConfere() { return nomeConfere; }
    public void setNomeConfere(Boolean nomeConfere) { this.nomeConfere = nomeConfere; }
    public String getBancoNome() { return bancoNome; }
    public void setBancoNome(String bancoNome) { this.bancoNome = bancoNome; }
    public Integer getPontuacaoRisco() { return pontuacaoRisco; }
    public void setPontuacaoRisco(Integer pontuacaoRisco) { this.pontuacaoRisco = pontuacaoRisco; }
    public Semaforo getResultado() { return resultado; }
    public void setResultado(Semaforo resultado) { this.resultado = resultado; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public List<String> getAlertas() { return alertas; }
    public void setAlertas(List<String> alertas) { this.alertas = alertas; }
    public List<Long> getRespostasSim() { return respostasSim; }
    public void setRespostasSim(List<Long> respostasSim) { this.respostasSim = respostasSim; }
    public ContatoConfiancaResponse getContatoConfianca() { return contatoConfianca; }
    public void setContatoConfianca(ContatoConfiancaResponse contatoConfianca) { this.contatoConfianca = contatoConfianca; }
    public LocalDateTime getDataVerificacao() { return dataVerificacao; }
    public void setDataVerificacao(LocalDateTime dataVerificacao) { this.dataVerificacao = dataVerificacao; }
}
