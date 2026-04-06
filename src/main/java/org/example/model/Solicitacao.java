package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solicitacao {
    private String protocolo;
    private String descricao;
    private String localizacao;
    private StatusSolicitacao status;
    private Usuario usuario;
    private Categoria categoria;
    private Prioridade prioridade;
    private LocalDateTime dataCriacao;
    private List<HistoricoStatus> historico;

    protected Solicitacao() {}

    public Solicitacao(String protocolo, String descricao, String localizacao, StatusSolicitacao status, Usuario usuario, Categoria categoria, Prioridade prioridade, List<HistoricoStatus> historico) {
        if (usuario != null && usuario.isAnonimo()) {
            if (descricao == null || descricao.length() < 20) {
                throw new IllegalArgumentException("Denuncias anonimas exigem uma descricao detalhada (minimo 20 caracteres) para prevencao de trotes.");
            }
            if (localizacao == null || localizacao.length() < 10) {
                throw new IllegalArgumentException("Denuncias anonimas exigem uma localizacao mais especifica.");
            }
        }

        this.protocolo = protocolo;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.status = status != null ? status : StatusSolicitacao.ABERTO;
        this.usuario = usuario;
        this.categoria = categoria;
        this.prioridade = prioridade != null ? prioridade : Prioridade.BAIXA;
        this.dataCriacao = LocalDateTime.now();
        this.historico = historico != null ? historico : new ArrayList<HistoricoStatus>();
    }

    public String getProtocolo() { return protocolo; }
    public String getDescricao() { return descricao; }
    public String getLocalizacao() { return localizacao; }
    public StatusSolicitacao getStatus() { return status; }
    public Usuario getUsuario() { return usuario; }
    public Categoria getCategoria() { return categoria; }
    public Prioridade getPrioridade() { return prioridade; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public List<HistoricoStatus> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public void atualizarStatus(StatusSolicitacao novoStatus, String responsavel, String comentario) {
        if (novoStatus == null) {
            throw new RuntimeException("Status nao pode ser nulo");
        }
        if (comentario == null || comentario.isBlank()) {
            throw new RuntimeException("Comentario eh obrigatorio");
        }
        if (this.status == novoStatus) {
            throw new RuntimeException("A solicitacao jah estah com esse status");
        }

        this.status = novoStatus;

        if (this.historico == null) {
            this.historico = new java.util.ArrayList<>();
        }

        HistoricoStatus novoHistorico = new HistoricoStatus(novoStatus, responsavel, comentario);
        this.historico.add(novoHistorico);
    }
}