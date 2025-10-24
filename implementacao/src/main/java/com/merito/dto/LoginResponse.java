package com.merito.dto;

public class LoginResponse {
    
    private String token;
    private String tipo;
    private String email;
    private String nome;

    // Constructors
    public LoginResponse() {
    }

    public LoginResponse(String token, String tipo, String email, String nome) {
        this.token = token;
        this.tipo = tipo;
        this.email = email;
        this.nome = nome;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
