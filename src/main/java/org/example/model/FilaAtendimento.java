package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilaAtendimento {
    private List<Solicitacao> fila;

    public FilaAtendimento() {
        this.fila = new ArrayList<>();
    }

    public void adicionar(Solicitacao solicitacao) {
        if (solicitacao == null) {
            throw new IllegalArgumentException("A solicitação nao pode ser nula");
        }
        this.fila.add(solicitacao);
    }

    public List<Solicitacao> listar() {
        return Collections.unmodifiableList(this.fila);
    }
}
