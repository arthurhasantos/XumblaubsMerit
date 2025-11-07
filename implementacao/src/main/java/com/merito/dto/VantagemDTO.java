package com.merito.dto;

import jakarta.validation.constraints.*;

public class VantagemDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 255, message = "Nome deve ter entre 2 e 255 caracteres")
    private String nome;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String descricao;
    
    private String fotoUrl;
    
    @NotNull(message = "Custo em moedas é obrigatório")
    @DecimalMin(value = "0.01", message = "Custo em moedas deve ser maior que zero")
    private Double custoEmMoedas;
    
    private Long empresaId;
    
    // Constructors
    public VantagemDTO() {
    }
    
    public VantagemDTO(String nome, String descricao, String fotoUrl, Double custoEmMoedas, Long empresaId) {
        this.nome = nome;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.custoEmMoedas = custoEmMoedas;
        this.empresaId = empresaId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getFotoUrl() {
        return fotoUrl;
    }
    
    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
    
    public Double getCustoEmMoedas() {
        return custoEmMoedas;
    }
    
    public void setCustoEmMoedas(Double custoEmMoedas) {
        this.custoEmMoedas = custoEmMoedas;
    }
    
    public Long getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
}



