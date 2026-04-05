package org.example.model;

import java.time.LocalDateTime;

public class HistoricoStatus {
    private StatusSolicitacao status;
    private LocalDateTime data;
    private String responsavel;
    private String comentario;

    protected HistoricoStatus() {}

    public HistoricoStatus(StatusSolicitacao status, String responsavel, String comentario) {
        this.status = status;
        this.data = LocalDateTime.now();
        this.responsavel = responsavel;
        this.comentario = comentario;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getComentario() {
        return comentario;
    }
}
