package com.merito.dto;

import java.time.LocalDateTime;

public class ResgateVantagemDTO {
    
    private Long id;
    
    private LocalDateTime dataResgate;
    
    private String codigoCupom;
    
    private Long alunoId;
    
    private String alunoNome;
    
    private Long vantagemId;
    
    private String vantagemNome;
    
    private String vantagemFotoUrl;
    
    private Double custoEmMoedas;
    
    // Constructors
    public ResgateVantagemDTO() {
    }
    
    public ResgateVantagemDTO(Long id, LocalDateTime dataResgate, String codigoCupom, 
                             Long alunoId, String alunoNome, Long vantagemId, 
                             String vantagemNome, Double custoEmMoedas) {
        this.id = id;
        this.dataResgate = dataResgate;
        this.codigoCupom = codigoCupom;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.vantagemId = vantagemId;
        this.vantagemNome = vantagemNome;
        this.custoEmMoedas = custoEmMoedas;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDataResgate() {
        return dataResgate;
    }
    
    public void setDataResgate(LocalDateTime dataResgate) {
        this.dataResgate = dataResgate;
    }
    
    public String getCodigoCupom() {
        return codigoCupom;
    }
    
    public void setCodigoCupom(String codigoCupom) {
        this.codigoCupom = codigoCupom;
    }
    
    public Long getAlunoId() {
        return alunoId;
    }
    
    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
    
    public String getAlunoNome() {
        return alunoNome;
    }
    
    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }
    
    public Long getVantagemId() {
        return vantagemId;
    }
    
    public void setVantagemId(Long vantagemId) {
        this.vantagemId = vantagemId;
    }
    
    public String getVantagemNome() {
        return vantagemNome;
    }
    
    public void setVantagemNome(String vantagemNome) {
        this.vantagemNome = vantagemNome;
    }
    
    public Double getCustoEmMoedas() {
        return custoEmMoedas;
    }
    
    public void setCustoEmMoedas(Double custoEmMoedas) {
        this.custoEmMoedas = custoEmMoedas;
    }
    
    public String getVantagemFotoUrl() {
        return vantagemFotoUrl;
    }
    
    public void setVantagemFotoUrl(String vantagemFotoUrl) {
        this.vantagemFotoUrl = vantagemFotoUrl;
    }
}

