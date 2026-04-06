package org.example.model;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private boolean anonimo;
    private boolean gestor;

    protected Usuario() {}

    public Usuario(Long id, String nome, String email, boolean anonimo, boolean gestor) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.anonimo = anonimo;
        this.gestor = gestor;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public boolean isGestor() {
        return gestor;
    }
}
