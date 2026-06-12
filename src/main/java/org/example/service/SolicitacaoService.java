package org.example.service;

import org.example.model.Solicitacao;
import org.example.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository repository;

    public SolicitacaoService(SolicitacaoRepository repository) {
        this.repository = repository;
    }

    public Solicitacao criarSolicitacao(Solicitacao solicitacao) {
        if (solicitacao.isAnonimo()) {
            if (solicitacao.getDescricao() == null || solicitacao.getDescricao().length() < 20) {
                throw new IllegalArgumentException("Denuncias anonimas exigem no minimo 20 caracteres");
            }
            solicitacao.setNomeCidadao("Anonimo");
            solicitacao.setEmailCidadao("anonimo@sistema.com");
        }

        solicitacao.setCodigo("REQ-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        solicitacao.setStatus("Aberto");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        solicitacao.setData(LocalDate.now().format(formatter));

        if (solicitacao.getPrioridade().contains("Alta")) {
            solicitacao.setSla("5 dias");
        } else if (solicitacao.getPrioridade().contains("Média") || solicitacao.getPrioridade().contains("Media")) {
            solicitacao.setSla("15 dias");
        } else {
            solicitacao.setSla("30 dias");
        }

        return repository.save(solicitacao);
    }

    public List<Solicitacao> listarTodas() {
        return repository.findAll();
    }

    public Solicitacao buscarPorCodigo(String codigo) {
        return repository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Protocolo nao encontrado"));
    }

    public Solicitacao atualizarStatus(String codigo, String novoStatus) {
        Solicitacao solicitacao = buscarPorCodigo(codigo);
        solicitacao.setStatus(novoStatus);
        return repository.save(solicitacao);
    }
}