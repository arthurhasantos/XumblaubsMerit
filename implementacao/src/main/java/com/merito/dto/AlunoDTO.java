package com.merito.dto;

import jakarta.validation.constraints.*;

public class AlunoDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;
    
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 000.000.000-00")
    private String cpf;
    
    @NotBlank(message = "RG é obrigatório")
    private String rg;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    private String endereco;
    
    @NotBlank(message = "Curso é obrigatório")
    @Size(max = 100, message = "Curso deve ter no máximo 100 caracteres")
    private String curso;
    
    @NotNull(message = "ID da instituição é obrigatório")
    private Long instituicaoId;
    
    private Double saldoMoedas = 0.0;
    
    // Constructors
    public AlunoDTO() {
    }
    
    public AlunoDTO(String nome, String email, String senha, String cpf, String rg, 
                   String endereco, String curso, Long instituicaoId) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.curso = curso;
        this.instituicaoId = instituicaoId;
        this.saldoMoedas = 0.0;
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
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
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
    
    public Long getInstituicaoId() {
        return instituicaoId;
    }
    
    public void setInstituicaoId(Long instituicaoId) {
        this.instituicaoId = instituicaoId;
    }
    
    public Double getSaldoMoedas() {
        return saldoMoedas;
    }
    
    public void setSaldoMoedas(Double saldoMoedas) {
        this.saldoMoedas = saldoMoedas;
    }
}
