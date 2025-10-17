package com.merito.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instituicao")
public class Instituicao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String endereco;
    
    @OneToMany(mappedBy = "instituicao", cascade = CascadeType.ALL)
    private List<Aluno> alunos = new ArrayList<>();
    
    @OneToMany(mappedBy = "instituicao", cascade = CascadeType.ALL)
    private List<Professor> professores = new ArrayList<>();

    // Constructors
    public Instituicao() {
    }

    public Instituicao(Long id, String nome, String endereco, List<Aluno> alunos, List<Professor> professores) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.alunos = alunos;
        this.professores = professores;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public void setProfessores(List<Professor> professores) {
        this.professores = professores;
    }
}

