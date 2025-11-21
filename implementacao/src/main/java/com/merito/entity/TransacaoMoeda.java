package com.merito.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacao_moeda")
public class TransacaoMoeda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime dataTransacao;
    
    @Column(nullable = false)
    private Double quantidade;
    
    @Column(nullable = false)
    private String tipo; // "ENVIADA" ou "RECEBIDA"
    
    @Column(length = 500)
    private String motivo;
    
    @ManyToOne
    @JoinColumn(name = "fk_professor", nullable = false)
    private Professor professor;
    
    @ManyToOne
    @JoinColumn(name = "fk_aluno", nullable = true)
    private Aluno aluno; // Nullable para transações do sistema (crédito inicial)

    // Constructors
    public TransacaoMoeda() {
    }

    public TransacaoMoeda(Long id, LocalDateTime dataTransacao, Double quantidade, 
                         String tipo, String motivo, Professor professor, Aluno aluno) {
        this.id = id;
        this.dataTransacao = dataTransacao;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.motivo = motivo;
        this.professor = professor;
        this.aluno = aluno;
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

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    @PrePersist
    public void prePersist() {
        if (dataTransacao == null) {
            dataTransacao = LocalDateTime.now();
        }
    }
}

