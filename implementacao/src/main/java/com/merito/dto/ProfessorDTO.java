package com.merito.dto;

public class ProfessorDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String departamento;
    private Double saldoMoedas = 0.0;
    private Long instituicaoId;
    private String instituicaoNome;
    
    // Constructors
    public ProfessorDTO() {
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public Double getSaldoMoedas() {
        return saldoMoedas;
    }
    
    public void setSaldoMoedas(Double saldoMoedas) {
        this.saldoMoedas = saldoMoedas;
    }
    
    public Long getInstituicaoId() {
        return instituicaoId;
    }
    
    public void setInstituicaoId(Long instituicaoId) {
        this.instituicaoId = instituicaoId;
    }
    
    public String getInstituicaoNome() {
        return instituicaoNome;
    }
    
    public void setInstituicaoNome(String instituicaoNome) {
        this.instituicaoNome = instituicaoNome;
    }
}

