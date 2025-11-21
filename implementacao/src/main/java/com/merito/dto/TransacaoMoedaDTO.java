package com.merito.dto;

import java.time.LocalDateTime;

public class TransacaoMoedaDTO {
    
    private Long id;
    private LocalDateTime dataTransacao;
    private Double quantidade;
    private String tipo; // "ENVIADA" ou "RECEBIDA"
    private String motivo;
    private Long professorId;
    private String professorNome;
    private Long alunoId;
    private String alunoNome;
    
    // Constructors
    public TransacaoMoedaDTO() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }
    
    public void setDataTransacao(LocalDateTime dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
    
    public Double getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public Long getProfessorId() {
        return professorId;
    }
    
    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }
    
    public String getProfessorNome() {
        return professorNome;
    }
    
    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
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
}

