package br.com.fiap.confereai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ContatoConfiancaResponse {
    private Long id;
    private String nome;
    private String telefone;
    private String parentesco;
    private Boolean principal;
    private Long usuarioId;

    @Schema(description = "Link para ligar com um toque no celular", example = "tel:+5511912345678")
    private String linkLigacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getLinkLigacao() { return linkLigacao; }
    public void setLinkLigacao(String linkLigacao) { this.linkLigacao = linkLigacao; }
}
