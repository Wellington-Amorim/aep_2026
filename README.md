# 🏛️ ObservaAção - Plataforma de Gestão de Demandas Cidadãs

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)
![JavaScript](https://img.shields.io/badge/JavaScript-Vanilla-yellow.svg)
![H2 Database](https://img.shields.io/badge/Database-H2%20In--Memory-blue.svg)

## 📌 Sobre o Projeto
O **ObservaAção** é um sistema *Web* desenvolvido para o registro, acompanhamento e gestão de solicitações e problemas reportados pelos cidadãos à administração pública (como buracos na via, iluminação defeituosa, etc.). 

O sistema foi desenhado com uma **arquitetura monolítica autossuficiente**, onde o *Back-End* (API RESTful) e o *Front-End* (páginas estáticas reativas) coexistem na mesma infraestrutura, eliminando a necessidade de servidores separados e problemas de compartilhamento de recursos (CORS).

## ⚙️ Arquitetura e Tecnologias
- **Back-End:** Java com Spring Boot (Data JPA, Web, Validation).
- **Front-End:** HTML5, CSS3 e JavaScript Vanilla (comunicação assíncrona via *Fetch API*). Integração direta no diretório `src/main/resources/static`.
- **Banco de Dados:** H2 Database em memória (volátil para ambiente de desenvolvimento e testes).
- **Validações:** Jakarta Bean Validation para integridade de dados e Expressões Regulares (Regex) para segurança de senhas.

## 🚀 Funcionalidades Principais
- **Registro e Autenticação:** Criação de usuários com validação rigorosa de segurança (mínimo de 8 caracteres, maiúsculas, minúsculas, números e símbolos).
- **Submissão de Protocolos:** Criação de solicitações públicas, com ou sem identificação (modo anônimo).
- **Painel do Cidadão:** Interface para acompanhamento em tempo real do status dos protocolos e cálculo dinâmico do SLA (Prazo de Atendimento).
- **Painel do Gestor:** *Dashboard* reservado para análise, atualização de status (Em Andamento, Concluído, Rejeitado) e triagem de demandas.

## 🛡️ Qualidade de Código e Segurança
O repositório foi submetido a uma auditoria de análise estática através do **SonarCloud**, atingindo métricas de excelência na engenharia de software:
- **Quality Gate:** Passed (Aprovado).
- **Reliability & Security:** Nota **A** (0 vulnerabilidades detectadas, com sanitização manual de entradas DOM contra ataques XSS).
- **Maintainability:** Nota **A** (Código estruturado sem repetições, respeitando o princípio DRY e com eliminação total de *zombie code*).

## 🛠️ Como Executar o Projeto
Por se tratar de um monolito, a execução é extremamente simples, não requerendo *Live Servers* ou configurações complexas de ambiente.

1. Clone o repositório para a sua máquina local:
> git clone https://github.com/SEU_USUARIO/aep_2026.git

2. Abra a pasta do projeto na sua IDE preferida (IntelliJ IDEA, Eclipse, ou VS Code com *Extension Pack for Java*).
3. Execute a classe principal `ObservaAcaoApplication.java`.
4. O servidor Tomcat embutido iniciará automaticamente na porta 8080.
5. Abra o navegador web e acesse a interface através do endereço:
> http://localhost:8080/index.html

*Nota: Por utilizar o banco H2 em memória, os dados submetidos são reiniciados sempre que a aplicação é encerrada.*

## 👤 Equipe
- [Wellington Amorim](https://github.com/Wellington-Amorim)
- [Matheus Previato](https://github.com/Matheus-Previato)
