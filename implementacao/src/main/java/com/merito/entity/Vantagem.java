package com.merito.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vantagem")
public class Vantagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 1000)
    private String descricao;
    
    @Column
    private String fotoUrl;
    
    @Column(nullable = false)
    private Double custoEmMoedas;
    
    @ManyToOne
    @JoinColumn(name = "fk_empresa", nullable = false)
    private EmpresaParceira empresa;
    
    @OneToMany(mappedBy = "vantagem", cascade = CascadeType.ALL)
    private List<ResgateVantagem> resgates = new ArrayList<>();

    // Constructors
    public Vantagem() {
    }

    public Vantagem(Long id, String nome, String descricao, String fotoUrl, 
                    Double custoEmMoedas, EmpresaParceira empresa) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.custoEmMoedas = custoEmMoedas;
        this.empresa = empresa;
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

    public EmpresaParceira getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaParceira empresa) {
        this.empresa = empresa;
    }

    public List<ResgateVantagem> getResgates() {
        return resgates;
    }

    public void setResgates(List<ResgateVantagem> resgates) {
        this.resgates = resgates;
    }
}

