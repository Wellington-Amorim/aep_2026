package org.example;

import org.example.model.*;
import org.example.util.CarregadorDePersonas;

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

        boolean executando = true;

        while (executando) {
            Usuario usuarioAtual = realizarAutenticacao(scanner, personas);

            if (usuarioAtual == null) {
                break;
            }

            System.out.println("\n>>> Logado como: " + usuarioAtual.getNome() + (usuarioAtual.isGestor() ? " (MODO GESTOR)" : " (MODO CIDADAO)") + " <<<");

            boolean menuAtivo = true;
            while (menuAtivo) {
                exibirMenuPrincipal(usuarioAtual);
                System.out.print("Escolha uma opcao: ");

                int opcao;
                try {
                    opcao = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Opcao invalida. Por favor, digite um numero.");
                    continue;
                }

                try {
                    switch (opcao) {
                        case 1:
                            cadastrarNovaSolicitacao(scanner, usuarioAtual, servico);
                            break;
                        case 2:
                            consultarSolicitacao(scanner, servico);
                            break;
                        case 3:
                            listarDemandasGestor(usuarioAtual, servico);
                            break;
                        case 4:
                            atualizarStatusGestor(scanner, usuarioAtual, servico);
                            break;
                        case 9:
                            System.out.println("\n>>> Fazendo logout e retornando ao menu inicial...");
                            menuAtivo = false;
                            break;
                        case 0:
                            System.out.println("\n>>> Encerrando o sistema ObservaAcao. Ate logo!");
                            menuAtivo = false;
                            executando = false;
                            break;
                        default:
                            System.out.println("Opcao invalida. Tente novamente.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("\n>>> ERRO DE VALIDACAO: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println("\n>>> ERRO/ACESSO NEGADO: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("\n>>> ERRO DESCONHECIDO: " + e.getMessage());
                }
            }
        }
        scanner.close();
    }

    // ========================================================================
    // (CLEAN CODE) - SEPARAÇÃO DE RESPONSABILIDADES
    // ========================================================================

    private static Usuario realizarAutenticacao(Scanner scanner, List<Usuario> personas) {
        while (true) {
            System.out.println("\nComo deseja acessar o sistema?");
            System.out.println("1. Fazer Cadastro (Novo Cidadao)");
            System.out.println("2. Entrar como Anonimo");
            System.out.println("3. Entrar usando uma Persona de Teste (IHC)");
            System.out.println("0. Encerrar Sistema");
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
                    System.out.println(">>> Cadastro realizado com sucesso!");
                    return new Usuario(System.currentTimeMillis(), nome, email, false, false);
                case 2:
                    System.out.println("\nVoce acessou o modo ANONIMO.");
                    System.out.println("Aviso: Regras mais rigidas de descricao serao aplicadas para evitar trotes.");
                    return new Usuario(999L, "CIDADAO ANONIMO", "oculto", true, false);
                case 3:
                    System.out.println("\n--- Selecione uma Persona ---");
                    for (int i = 0; i < personas.size(); i++) {
                        String tipo = personas.get(i).isGestor() ? "[GESTOR]" : "[CIDADAO]";
                        System.out.println((i + 1) + ". " + tipo + " " + personas.get(i).getNome());
                    }
                    System.out.print("Escolha o numero da persona: ");
                    try {
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        return personas.get(index);
                    } catch (Exception e) {
                        System.out.println("Opcao invalida.");
                    }
                    break;
                case 0:
                    return null;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static void exibirMenuPrincipal(Usuario usuarioAtual) {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Cadastrar nova solicitacao");
        System.out.println("2. Consultar solicitacao por protocolo (Rastreio Cidadao)");
        if (usuarioAtual.isGestor()) {
            System.out.println("3. Listar TODAS as solicitacoes da cidade (Painel Gestor)");
            System.out.println("4. Atualizar status de uma solicitacao (Painel Gestor)");
        }
        System.out.println("9. Trocar de Usuario (Fazer Logout)");
        System.out.println("0. Encerrar Sistema");
    }

    private static void cadastrarNovaSolicitacao(Scanner scanner, Usuario usuarioAtual, ServicoSolicitacoes servico) {
        System.out.println("\n-- Nova Solicitacao --");
        if (usuarioAtual.isAnonimo()) {
            System.out.println("Lembrete: Como anonimo, seja detalhista (minimo 20 caracteres na descricao e 10 na localizacao).");
        }

        System.out.println("\nEscolha a Categoria:");
        System.out.println("1. Iluminacao | 2. Buraco na via | 3. Limpeza urbana | 4. Saude | 5. Seguranca Escolar | 6. Denuncia | 7. Outros");
        System.out.print("Opcao: ");
        int catOpcao = Integer.parseInt(scanner.nextLine());

        String nomeCategoria = switch (catOpcao) {
            case 1 -> "Iluminacao";
            case 2 -> "Buraco na via";
            case 3 -> "Limpeza urbana";
            case 4 -> "Saude";
            case 5 -> "Seguranca Escolar";
            case 6 -> "Denuncia de Abuso/Irregularidade";
            case 7 -> {
                System.out.print("Digite a categoria desejada: ");
                yield scanner.nextLine();
            }
            default -> "Geral";
        };

        Categoria categoriaSelecionada = new Categoria((long) catOpcao, nomeCategoria);

        System.out.print("Descreva o problema: ");
        String descricao = scanner.nextLine();
        System.out.print("Qual a localizacao (ex: Rua principal, Bairro Centro)? ");
        String localizacao = scanner.nextLine();

        System.out.println("Escolha a Prioridade do chamado:");
        System.out.println("1 - BAIXA | 2 - MEDIA | 3 - ALTA | 4 - URGENTE");
        System.out.print("Opcao: ");
        int prioOpcao = Integer.parseInt(scanner.nextLine());

        Prioridade prioridadeEscolhida = switch (prioOpcao) {
            case 2 -> Prioridade.MEDIA;
            case 3 -> Prioridade.ALTA;
            case 4 -> Prioridade.URGENTE;
            default -> Prioridade.BAIXA;
        };

        String protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Solicitacao nova = new Solicitacao(
                protocolo, descricao, localizacao, StatusSolicitacao.ABERTO,
                usuarioAtual, categoriaSelecionada, prioridadeEscolhida, null
        );

        servico.criar(nova);
        System.out.println("\n>>> Sucesso! O numero do seu protocolo eh: " + protocolo);
        System.out.println(">>> O prazo alvo (SLA) para esta prioridade eh de " + prioridadeEscolhida.getPrazoSlaDias() + " dias.");
    }

    private static void consultarSolicitacao(Scanner scanner, ServicoSolicitacoes servico) {
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
    }

    private static void listarDemandasGestor(Usuario usuarioAtual, ServicoSolicitacoes servico) {
        if (!usuarioAtual.isGestor()) {
            throw new RuntimeException("Acesso negado. Apenas Gestores podem listar todas as demandas da cidade.");
        }

        List<Solicitacao> lista = servico.listar();
        if (lista.isEmpty()) {
            System.out.println("Ainda nao existem solicitacoes cadastradas.");
        } else {
            System.out.println("\n--- Lista Geral de Solicitacoes (PAINEL GESTOR) ---");
            for (Solicitacao s : lista) {
                System.out.println("[" + s.getProtocolo() + "] " + s.getCategoria().getNome() + " | " + s.getPrioridade() + " | Status: " + s.getStatus() + " | Autor: " + s.getUsuario().getNome());
            }
        }
    }

    private static void atualizarStatusGestor(Scanner scanner, Usuario usuarioAtual, ServicoSolicitacoes servico) {
        if (!usuarioAtual.isGestor()) {
            throw new RuntimeException("Acesso negado. Apenas Gestores podem alterar o status.");
        }

        System.out.print("Digite o protocolo da solicitacao que deseja atualizar: ");
        String protAtualizar = scanner.nextLine();

        System.out.println("Escolha o novo status:");
        System.out.println("1 - TRIAGEM | 2 - EM_EXECUCAO | 3 - RESOLVIDO | 4 - ENCERRADO");
        System.out.print("Opcao: ");
        int statusOpcao = Integer.parseInt(scanner.nextLine());

        StatusSolicitacao novoStatus = switch (statusOpcao) {
            case 1 -> StatusSolicitacao.TRIAGEM;
            case 2 -> StatusSolicitacao.EM_EXECUCAO;
            case 3 -> StatusSolicitacao.RESOLVIDO;
            case 4 -> StatusSolicitacao.ENCERRADO;
            default -> throw new IllegalArgumentException("Status invalido.");
        };

        System.out.print("Comentario / Justificativa (obrigatorio): ");
        String comentario = scanner.nextLine();

        servico.atualizarStatus(protAtualizar, novoStatus, usuarioAtual.getNome(), comentario);
        System.out.println(">>> Status atualizado com sucesso!");
    }
}