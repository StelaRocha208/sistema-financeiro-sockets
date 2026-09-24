# Sistema Financeiro com Sockets TCP

Sistema financeiro desenvolvido em Java utilizando a arquitetura cliente-servidor e comunicação por sockets TCP.

O sistema permite criar contas e realizar operações financeiras básicas, como consulta de saldo, depósito e saque. O servidor atua como autoridade central, sendo responsável pelo armazenamento das contas e pelo processamento das operações.

## 📌 Objetivo

Desenvolver uma aplicação distribuída utilizando sockets TCP, colocando em prática conceitos de:

- Comunicação em rede;
- Arquitetura cliente-servidor;
- Programação com sockets;
- Controle centralizado do estado das contas;
- Gerenciamento de estado compartilhado;
- Consistência dos saldos;
- Processamento de operações financeiras;
- Desenvolvimento de um protocolo simples de transação;
- Validação das operações.

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Sockets TCP**
- **Debian 13**
- **VirtualBox**
- **Git**
- **GitHub**

## 🏗️ Arquitetura

O sistema é dividido em duas aplicações:

### Servidor

Responsável por:

- Aceitar a conexão do cliente;
- Armazenar as contas bancárias;
- Criar contas;
- Consultar saldos;
- Processar depósitos;
- Processar saques;
- Validar as operações;
- Enviar as respostas para o cliente.

### Cliente

Responsável por:

- Conectar-se ao servidor;
- Apresentar a interface de comandos;
- Solicitar as informações das operações;
- Enviar os comandos através do socket TCP;
- Exibir as respostas recebidas do servidor.

A comunicação entre cliente e servidor é realizada através do protocolo **TCP**.

## 🌐 Configuração da rede

O projeto foi executado utilizando duas máquinas virtuais no VirtualBox, conectadas através de uma rede **Host-only**.

| Máquina | Função | Endereço IP |
|---|---|---|
| Banco-Servidor | Servidor | `192.168.56.10` |
| Banco-Cliente | Cliente | `192.168.56.11` |

A porta utilizada para a comunicação é:

```text
5000
