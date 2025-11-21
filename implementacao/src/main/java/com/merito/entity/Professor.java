package com.merito.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "professor")
public class Professor extends Usuario {
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @Column(nullable = false)
    private String departamento;
    
    @Column(nullable = false)
    private Double saldoMoedas = 1000.0; // Saldo inicial padrão de 1000 moedas
    
    @ManyToOne
    @JoinColumn(name = "fk_instituicao", nullable = false)
    private Instituicao instituicao;

    // Constructors
    public Professor() {
    }

    public Professor(Long id, String email, String senha, String tipoUsuario,
                     String nome, String cpf, String departamento, 
                     Double saldoMoedas, Instituicao instituicao) {
        super(id, email, senha, tipoUsuario);
        this.nome = nome;
        this.cpf = cpf;
        this.departamento = departamento;
        this.saldoMoedas = saldoMoedas;
        this.instituicao = instituicao;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
}

