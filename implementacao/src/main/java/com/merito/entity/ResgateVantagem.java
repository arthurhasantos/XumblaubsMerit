package com.merito.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resgate_vantagem")
public class ResgateVantagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime dataResgate;
    
    @Column(nullable = false, unique = true)
    private String codigoCupom;
    
    @ManyToOne
    @JoinColumn(name = "fk_aluno", nullable = false)
    private Aluno aluno;
    
    @ManyToOne
    @JoinColumn(name = "fk_vantagem", nullable = false)
    private Vantagem vantagem;

    // Constructors
    public ResgateVantagem() {
    }

    public ResgateVantagem(Long id, LocalDateTime dataResgate, String codigoCupom, 
                           Aluno aluno, Vantagem vantagem) {
        this.id = id;
        this.dataResgate = dataResgate;
        this.codigoCupom = codigoCupom;
        this.aluno = aluno;
        this.vantagem = vantagem;
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

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Vantagem getVantagem() {
        return vantagem;
    }

    public void setVantagem(Vantagem vantagem) {
        this.vantagem = vantagem;
    }
    
    @PrePersist
    public void prePersist() {
        if (dataResgate == null) {
            dataResgate = LocalDateTime.now();
        }
    }
}

