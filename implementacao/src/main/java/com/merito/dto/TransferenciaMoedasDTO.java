package com.merito.dto;

import jakarta.validation.constraints.*;

public class TransferenciaMoedasDTO {
    
    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;
    
    @NotNull(message = "Quantidade de moedas é obrigatória")
    @Min(value = 0, message = "Quantidade de moedas deve ser maior ou igual a zero")
    @Positive(message = "Quantidade de moedas deve ser maior que zero")
    private Double quantidade;
    
    private String motivo; // Opcional: motivo da transferência
    
    // Constructors
    public TransferenciaMoedasDTO() {
    }
    
    public TransferenciaMoedasDTO(Long alunoId, Double quantidade, String motivo) {
        this.alunoId = alunoId;
        this.quantidade = quantidade;
        this.motivo = motivo;
    }
    
    // Getters and Setters
    public Long getAlunoId() {
        return alunoId;
    }
    
    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
    
    public Double getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}

