package com.merito.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresa_parceira")
public class EmpresaParceira extends Usuario {
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String cnpj;
    
    @Column(nullable = false)
    private String emailContato;
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Vantagem> vantagens = new ArrayList<>();

    // Constructors
    public EmpresaParceira() {
    }

    public EmpresaParceira(Long id, String email, String senha, String tipoUsuario,
                           String nome, String cnpj, String emailContato) {
        super(id, email, senha, tipoUsuario);
        this.nome = nome;
        this.cnpj = cnpj;
        this.emailContato = emailContato;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public List<Vantagem> getVantagens() {
        return vantagens;
    }

    public void setVantagens(List<Vantagem> vantagens) {
        this.vantagens = vantagens;
    }
}

