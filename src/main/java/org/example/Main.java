package org.example;

import org.example.model.*;
import util.CarregadorDePersonas;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServicoSolicitacoes servico = new ServicoSolicitacoes();
        List<Usuario> personas = CarregadorDePersonas.carregar();

        System.out.println("=========================================");
        System.out.println("  BEM-VINDO AO SISTEMA OBSERVA ACAO");
        System.out.println("=========================================");

        Usuario usuarioAtual = null;

        while (usuarioAtual == null) {
            System.out.println("\nComo deseja acessar o sistema?");
            System.out.println("1. Fazer Cadastro (Novo Usuario)");
            System.out.println("2. Entrar como Anonimo");
            System.out.println("3. Entrar usando uma Persona de Teste (IHC)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");

            int opLogin;
            try {
                opLogin = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida. Digite um numero.");
                continue;
            }

            switch (opLogin) {
                case 1:
                    System.out.println("\n--- Cadastro de Cidadao ---");
                    System.out.print("Digite seu nome completo: ");
                    String nome = scanner.nextLine();
                    System.out.print("Digite seu email: ");
                    String email = scanner.nextLine();
                    usuarioAtual = new Usuario(System.currentTimeMillis(), nome, email, false);
                    System.out.println("Cadastro realizado com sucesso!");
                    break;
                case 2:
                    System.out.println("\nVoce acessou o modo ANONIMO.");
                    System.out.println("Aviso: Regras mais rigidas de descricao serao aplicadas para evitar trotes.");
                    usuarioAtual = new Usuario(999L, "CIDADAO ANONIMO", "oculto", true);
                    break;
                case 3:
                    System.out.println("\n--- Selecione uma Persona ---");
                    for (int i = 0; i < personas.size(); i++) {
                        System.out.println((i + 1) + ". " + personas.get(i).getNome());
                    }
                    System.out.print("Escolha o numero da persona: ");
                    try {
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        usuarioAtual = personas.get(index);
                    } catch (Exception e) {
                        System.out.println("Opcao invalida.");
                    }
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }

        System.out.println("\n>>> Logado como: " + usuarioAtual.getNome() + " <<<");

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar nova solicitacao");
            System.out.println("2. Consultar solicitacao por protocolo");
            System.out.println("3. Listar todas as solicitacoes");
            System.out.println("4. Atualizar status de uma solicitacao (Gestores)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida. Por favor, digite um numero.");
                continue;
            }

            try {
                switch (opcao) {
                    case 1:
                        System.out.println("\n-- Nova Solicitacao --");
                        if (usuarioAtual.isAnonimo()) {
                            System.out.println("Lembrete: Como anonimo, seja detalhista (minimo 20 caracteres na descricao e 10 na localizacao).");
                        }

                        System.out.println("\nEscolha a Categoria:");
                        System.out.println("1. Iluminacao");
                        System.out.println("2. Buraco na via");
                        System.out.println("3. Limpeza urbana");
                        System.out.println("4. Saude");
                        System.out.println("5. Seguranca Escolar");
                        System.out.println("6. Outros (Escrever)");
                        System.out.print("Opcao: ");

                        int catOpcao = Integer.parseInt(scanner.nextLine());
                        String nomeCategoria = "";

                        switch (catOpcao) {
                            case 1: nomeCategoria = "Iluminacao"; break;
                            case 2: nomeCategoria = "Buraco na via"; break;
                            case 3: nomeCategoria = "Limpeza urbana"; break;
                            case 4: nomeCategoria = "Saude"; break;
                            case 5: nomeCategoria = "Seguranca Escolar"; break;
                            case 6:
                                System.out.print("Digite a categoria desejada: ");
                                nomeCategoria = scanner.nextLine();
                                break;
                            default:
                                System.out.println("Categoria invalida, definindo como 'Geral'.");
                                nomeCategoria = "Geral";
                        }

                        Categoria categoriaSelecionada = new Categoria((long) catOpcao, nomeCategoria);

                        System.out.print("Descreva o problema: ");
                        String descricao = scanner.nextLine();
                        System.out.print("Qual a localizacao (ex: Rua principal, Bairro Centro)? ");
                        String localizacao = scanner.nextLine();

                        System.out.println("Escolha a Prioridade do chamado:");
                        System.out.println("1 - BAIXA | 2 - MEDIA | 3 - ALTA | 4 - URGENTE");
                        System.out.print("Opcao: ");
                        int prioOpcao = Integer.parseInt(scanner.nextLine());

                        Prioridade prioridadeEscolhida = Prioridade.BAIXA;
                        switch (prioOpcao) {
                            case 1: prioridadeEscolhida = Prioridade.BAIXA; break;
                            case 2: prioridadeEscolhida = Prioridade.MEDIA; break;
                            case 3: prioridadeEscolhida = Prioridade.ALTA; break;
                            case 4: prioridadeEscolhida = Prioridade.URGENTE; break;
                        }

                        String protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                        Solicitacao nova = new Solicitacao(
                                protocolo, descricao, localizacao, StatusSolicitacao.ABERTO,
                                usuarioAtual, categoriaSelecionada, prioridadeEscolhida, null
                        );

                        servico.criar(nova);
                        System.out.println("\n>>> Sucesso! O numero do seu protocolo eh: " + protocolo);
                        System.out.println(">>> O prazo alvo (SLA) para esta prioridade eh de " + prioridadeEscolhida.getPrazoSlaDias() + " dias.");
                        break;

                    case 2:
                        System.out.print("Digite o protocolo: ");
                        String protBusca = scanner.nextLine();
                        Solicitacao encontrada = servico.buscarPorProtocolo(protBusca);

                        System.out.println("\n--- Detalhes da Solicitacao ---");
                        System.out.println("Protocolo: " + encontrada.getProtocolo());
                        System.out.println("Categoria: " + encontrada.getCategoria().getNome());
                        System.out.println("Descricao: " + encontrada.getDescricao());
                        System.out.println("Localizacao: " + encontrada.getLocalizacao());
                        System.out.println("Prioridade: " + encontrada.getPrioridade() + " (SLA: " + encontrada.getPrioridade().getPrazoSlaDias() + " dias)");
                        System.out.println("Status Atual: " + encontrada.getStatus());

                        System.out.println("Historico de Status:");
                        if (encontrada.getHistorico().isEmpty()) {
                            System.out.println("- Sem movimentacoes.");
                        } else {
                            encontrada.getHistorico().forEach(h ->
                                    System.out.println("- " + h.getData().toLocalDate() + ": " + h.getStatus() + " | Resp: " + h.getResponsavel() + " | Obs: " + h.getComentario())
                            );
                        }
                        break;

                    case 3:
                        List<Solicitacao> lista = servico.listar();
                        if (lista.isEmpty()) {
                            System.out.println("Ainda nao existem solicitacoes cadastradas.");
                        } else {
                            System.out.println("\n--- Lista de Solicitacoes ---");
                            for (Solicitacao s : lista) {
                                System.out.println("[" + s.getProtocolo() + "] " + s.getCategoria().getNome() + " | " + s.getPrioridade() + " | Status: " + s.getStatus() + " | Autor: " + s.getUsuario().getNome());
                            }
                        }
                        break;

                    case 4:
                        System.out.print("Digite o protocolo da solicitacao que deseja atualizar: ");
                        String protAtualizar = scanner.nextLine();

                        System.out.println("Escolha o novo status:");
                        System.out.println("1 - TRIAGEM | 2 - EM_EXECUCAO | 3 - RESOLVIDO | 4 - ENCERRADO");
                        System.out.print("Opcao: ");
                        int statusOpcao = Integer.parseInt(scanner.nextLine());

                        StatusSolicitacao novoStatus = StatusSolicitacao.ABERTO;
                        switch (statusOpcao) {
                            case 1: novoStatus = StatusSolicitacao.TRIAGEM; break;
                            case 2: novoStatus = StatusSolicitacao.EM_EXECUCAO; break;
                            case 3: novoStatus = StatusSolicitacao.RESOLVIDO; break;
                            case 4: novoStatus = StatusSolicitacao.ENCERRADO; break;
                            default:
                                System.out.println("Status invalido.");
                                continue;
                        }

                        System.out.print("Comentario / Justificativa (obrigatorio): ");
                        String comentario = scanner.nextLine();

                        servico.atualizarStatus(protAtualizar, novoStatus, usuarioAtual.getNome(), comentario);
                        System.out.println(">>> Status atualizado com sucesso!");
                        break;

                    case 0:
                        System.out.println("Encerrando o sistema ObservaAcao. Ate logo!");
                        break;

                    default:
                        System.out.println("Opcao invalida. Tente novamente.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("\n>>> ERRO DE VALIDACAO: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n>>> ERRO: " + e.getMessage());
            }
        }

        scanner.close();
    }
}