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

O sistema é dividido em duas aplicações.

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

```
5000
```

## 📂 Estrutura do projeto

```
sistema-financeiro-sockets/
├── contas/
├── src/
│   ├── client/
│   │   └── Cliente.java
│   └── server/
│       └── Servidor.java
├── transacoes.txt
└── README.md
```

A pasta `contas/` e o arquivo `transacoes.txt` são utilizados pelo servidor para persistência e registro das operações.

## ▶️ Como executar

Os comandos abaixo devem ser executados na raiz do projeto.

### Servidor

```bash
javac src/server/Servidor.java
java -cp src/server Servidor
```

Informe quando solicitado:

```
IP: 192.168.56.10
Porta: 5000
```

### Cliente

Em outra máquina virtual:

```bash
javac src/client/Cliente.java
java -cp src/client Cliente
```

Informe quando solicitado:

```
IP do servidor: 192.168.56.10
Porta: 5000
```

## 💰 Operações

O cliente disponibiliza as seguintes operações:

- Criar conta
- Ver saldo
- Depositar
- Sacar
- Sair

O servidor valida as operações e retorna mensagens de sucesso ou falha.

## 💾 Persistência de dados

As contas são armazenadas individualmente na pasta `contas/`. Ao iniciar o servidor, os dados existentes são carregados automaticamente.

## 📝 Log de transações

As operações de depósito e saque também são registradas no arquivo `transacoes.txt`, incluindo a conta, operação, valor e resultado.

## 📡 Protocolo

A comunicação entre cliente e servidor utiliza TCP. Os comandos são enviados pelo cliente e processados pelo servidor, que retorna uma resposta correspondente à operação solicitada.
