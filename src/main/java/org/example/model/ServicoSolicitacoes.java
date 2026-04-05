package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServicoSolicitacoes {
    private List<Solicitacao> banco;

    public ServicoSolicitacoes() {
        this.banco = new ArrayList<>();
    }

    public Solicitacao criar(Solicitacao solicitacao) {
        if (solicitacao == null) {
            throw new IllegalArgumentException("A solicitacao noo pode ser nula.");
        }
        this.banco.add(solicitacao);

        return solicitacao;
    }

    public List<Solicitacao> listar() {
        return Collections.unmodifiableList(this.banco);
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        if (protocolo == null || protocolo.isBlank()) {
            throw new IllegalArgumentException("O protocolo deve ser informado.");
        }

        for (Solicitacao solicitacao : this.banco) {
            if (solicitacao.getProtocolo().equals(protocolo)) {
                return solicitacao;
            }
        }

        throw new RuntimeException("Solicitacao com protocolo " + protocolo + " nao encontrada.");
    }

    public void atualizarStatus(String protocolo, StatusSolicitacao status, String responsavel, String comentario) {
        Solicitacao solicitacao = buscarPorProtocolo(protocolo);

        solicitacao.atualizarStatus(status, responsavel, comentario);
    }
}
