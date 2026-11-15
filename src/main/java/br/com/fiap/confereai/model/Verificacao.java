package br.com.fiap.confereai.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "verificacoes")
public class Verificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento", length = 10, nullable = false)
    private TipoPagamento tipoPagamento;

    @Column(name = "cnpj", length = 14, nullable = false)
    private String cnpj;

    @Column(name = "nome_informado", length = 150)
    private String nomeInformado;

    @Column(name = "codigo_banco", length = 3)
    private String codigoBanco;

    @Column(name = "valor", precision = 15, scale = 2)
    private BigDecimal valor;


    @Column(name = "razao_social", length = 200)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 200)
    private String nomeFantasia;

    @Column(name = "situacao_cadastral", length = 50)
    private String situacaoCadastral;

    @Column(name = "data_abertura")
    private LocalDate dataAbertura;

    @Column(name = "nome_confere")
    private Boolean nomeConfere;

    @Column(name = "banco_nome", length = 200)
    private String bancoNome;

    @Column(name = "pontuacao_risco", nullable = false)
    private Integer pontuacaoRisco;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", length = 10, nullable = false)
    private Semaforo resultado;

   
    @Column(name = "alertas", length = 4000)
    private String alertas;

    @Column(name = "data_verificacao", nullable = false)
    private LocalDateTime dataVerificacao;

  
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "verificacoes_sinais",
            joinColumns = @JoinColumn(name = "verificacao_id"),
            inverseJoinColumns = @JoinColumn(name = "pergunta_id"))
    private Set<PerguntaGolpe> sinaisMarcados = new LinkedHashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

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

    public String getAlertas() { return alertas; }
    public void setAlertas(String alertas) { this.alertas = alertas; }

    public LocalDateTime getDataVerificacao() { return dataVerificacao; }
    public void setDataVerificacao(LocalDateTime dataVerificacao) { this.dataVerificacao = dataVerificacao; }

    public Set<PerguntaGolpe> getSinaisMarcados() { return sinaisMarcados; }
    public void setSinaisMarcados(Set<PerguntaGolpe> sinaisMarcados) { this.sinaisMarcados = sinaisMarcados; }
}
