package org.example.model;

public enum Prioridade {
    BAIXA(30),
    MEDIA(15),
    ALTA(5),
    URGENTE(2);

    private final int prazoSlaDias;

    Prioridade(int prazoSlaDias) {
        this.prazoSlaDias = prazoSlaDias;
    }

    public int getPrazoSlaDias() {
        return prazoSlaDias;
    }
}