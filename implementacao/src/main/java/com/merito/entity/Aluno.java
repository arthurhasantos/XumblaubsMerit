package com.merito.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno")
public class Aluno extends Usuario {
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @Column(nullable = false)
    private String rg;
    
    @Column(nullable = false)
    private String endereco;
    
    @Column(nullable = false)
    private String curso;
    
    @Column(nullable = false)
    private Double saldoMoedas = 0.0;
    
    @ManyToOne
    @JoinColumn(name = "fk_instituicao", nullable = false)
    private Instituicao instituicao;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<ResgateVantagem> resgates = new ArrayList<>();

    // Constructors
    public Aluno() {
    }

    public Aluno(Long id, String email, String senha, String tipoUsuario, 
                 String nome, String cpf, String rg, String endereco, 
                 String curso, Double saldoMoedas, Instituicao instituicao) {
        super(id, email, senha, tipoUsuario);
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.curso = curso;
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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
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

    public List<ResgateVantagem> getResgates() {
        return resgates;
    }

    public void setResgates(List<ResgateVantagem> resgates) {
        this.resgates = resgates;
    }
}

