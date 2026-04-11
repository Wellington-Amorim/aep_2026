# ObservaAção - AEP 2026

Repositório do projeto ObservaAção.

## Sobre o Projeto
O ObservaAção é um sistema de zeladoria urbana focado no cidadão. O objetivo é facilitar o registro de demandas públicas (como buracos na via, falta de iluminação ou denúncias de irregularidades) e dar transparência ao processo de atendimento. O projeto foi desenhado com base no **ODS 16** (Paz, Justiça e Instituições Eficazes).

Esta é a **1ª Entrega (Versão Beta)**. O sistema roda via linha de comando (CLI) e foi construído focado na aplicação de Programação Orientada a Objetos (POO) e Clean Code.

## Funcionalidades
- **Cadastro de Solicitações:** Criação de chamados com categoria, descrição, localização e prioridade (que define o prazo alvo/SLA).
- **Modo Anônimo e Prevenção de Abuso:** Opção de denúncia anônima com validações restritas (mínimo de caracteres) no construtor da classe para evitar trotes.
- **Rastreio pelo Cidadão:** Busca de chamados através de número de protocolo gerado automaticamente.
- **Painel do Gestor (Controle de Acesso):** Menu restrito a perfis administradores, permitindo listar todas as demandas e alterar o andamento.
- **Auditoria de Status:** Toda mudança de status (Triagem, Em Execução, Resolvido, Encerrado) exige comentário obrigatório e registra um histórico imutável.

## Tecnologias Utilizadas
- Java
- Orientação a Objetos (Sem uso de frameworks nesta etapa)
- Java Collections (Armazenamento de dados em memória)

## Como Executar
1. Clone este repositório no seu computador:
   `git clone <https://github.com/Wellington-Amorim/aep_2026.git>`
2. Abra a pasta do projeto na sua IDE (recomendado: IntelliJ IDEA ou Eclipse).
3. Localize e execute a classe principal em: `src/main/java/org/example/Main.java`.
4. O sistema iniciará no console/terminal da própria IDE.
5. *Nota para testes:* O sistema já inicia com 9 Personas pré-carregadas na memória para facilitar a navegação entre perfis de Cidadão e Gestor.

## Equipe
- [Wellington Amorim](https://github.com/Wellington-Amorim)
- [Matheus Previato](https://github.com/Matheus-Previato)