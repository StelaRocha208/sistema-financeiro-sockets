
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Locale;

public class Servidor {

    // Armazena o número da conta e o saldo correspondente
    static Map<String, Double> contas = new HashMap<>();

    public static void main(String[] args) {

        // Scanner usado para receber as configurações digitadas pelo usuário
        Scanner scanner = new Scanner(System.in);

        // Solicita o endereço IP e a porta que o servidor irá utilizar
        System.out.print("Digite o IP do servidor: ");
        String ip = scanner.nextLine();

        System.out.print("Digite a porta: ");
        int porta = scanner.nextInt();

        try {
            // Cria o socket TCP do servidor
            ServerSocket servidor = new ServerSocket(
                    porta,
                    50,
                    InetAddress.getByName(ip)
            );

            System.out.println("Servidor iniciado!");
            System.out.println("IP: " + ip);
            System.out.println("Porta: " + porta);
            System.out.println("Aguardando conexão...");

            // Aceita a conexão do cliente
            Socket cliente = servidor.accept();

            System.out.println("Cliente conectado!");
            System.out.println("IP do cliente: "
                    + cliente.getInetAddress().getHostAddress());

            // Entrada de dados recebidos do cliente
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(cliente.getInputStream())
            );

            // Saída de dados enviados para o cliente
            PrintWriter saida = new PrintWriter(
                    cliente.getOutputStream(), true
            );

            String mensagem;

            // Fica aguardando os comandos enviados pelo cliente
            while ((mensagem = entrada.readLine()) != null) {

                System.out.println("Comando recebido: " + mensagem);

                // Encerra a conexão quando o cliente enviar /exit
                if (mensagem.equalsIgnoreCase("/exit")) {
                    saida.println("[SUCESSO] Conexão encerrada.");
                    break;
                }

                // Divide o comando em partes
                String[] partes = mensagem.trim().split("\\s+");

                // Verifica se foi enviado algum comando
                if (partes.length == 0 || partes[0].isEmpty()) {
                    saida.println("[FALHA] Comando vazio.");
                    continue;
                }

                // Identifica qual operação o cliente solicitou
                String comando = partes[0].toUpperCase();

                // =========================
                // CRIAR CONTA
                // =========================
                if (comando.equals("CRIAR_CONTA")) {

                    if (partes.length != 2) {
                        saida.println(
                                "[FALHA] Informe o número da conta."
                        );
                        continue;
                    }

                    String numeroConta = partes[1];

                    // Verifica se a conta já existe
                    if (contas.containsKey(numeroConta)) {

                        saida.println("[FALHA] Conta já existe.");

                    } else {

                        // Cria a conta com saldo inicial zero
                        contas.put(numeroConta, 0.0);

                        saida.println("[SUCESSO] Conta criada.");
                    }

                // =========================
                // VER SALDO
                // =========================
                } else if (comando.equals("SALDO")) {

                    if (partes.length != 2) {
                        saida.println(
                                "[FALHA] Informe o número da conta."
                        );
                        continue;
                    }

                    String numeroConta = partes[1];

                    // Verifica se a conta existe
                    if (!contas.containsKey(numeroConta)) {

                        saida.println("[FALHA] Conta não encontrada.");

                    } else {

                        double saldo = contas.get(numeroConta);

                        saida.println(
                                "[SUCESSO] Saldo: "
                                + formatarValor(saldo)
                        );
                    }

                // =========================
                // DEPOSITAR
                // =========================
                } else if (comando.equals("DEPOSITAR")) {

                    if (partes.length != 3) {
                        saida.println(
                                "[FALHA] Informe a conta e o valor."
                        );
                        continue;
                    }

                    String numeroConta = partes[1];

                    // Verifica se a conta existe
                    if (!contas.containsKey(numeroConta)) {

                        saida.println("[FALHA] Conta não encontrada.");
                        continue;
                    }

                    try {

                        double valor = Double.parseDouble(partes[2]);

                        // O valor do depósito deve ser positivo
                        if (valor <= 0) {
                            saida.println(
                                    "[FALHA] O valor deve ser maior que zero."
                            );
                            continue;
                        }

                        // Obtém o saldo atual
                        double saldoAtual = contas.get(numeroConta);

                        // Atualiza o saldo
                        double novoSaldo = saldoAtual + valor;

                        contas.put(numeroConta, novoSaldo);

                        // Retorna apenas a mensagem de sucesso
                        saida.println("[SUCESSO] Depósito realizado.");

                    } catch (NumberFormatException e) {

                        saida.println(
                                "[FALHA] Valor inválido."
                        );
                    }

                // =========================
                // SACAR
                // =========================
                } else if (comando.equals("SACAR")) {

                    if (partes.length != 3) {
                        saida.println(
                                "[FALHA] Informe a conta e o valor."
                        );
                        continue;
                    }

                    String numeroConta = partes[1];

                    // Verifica se a conta existe
                    if (!contas.containsKey(numeroConta)) {

                        saida.println("[FALHA] Conta não encontrada.");
                        continue;
                    }

                    try {

                        double valor = Double.parseDouble(partes[2]);

                        // O valor do saque deve ser positivo
                        if (valor <= 0) {
                            saida.println(
                                    "[FALHA] O valor deve ser maior que zero."
                            );
                            continue;
                        }

                        // Obtém o saldo atual
                        double saldoAtual = contas.get(numeroConta);

                        // Verifica se existe saldo suficiente
                        if (valor > saldoAtual) {

                            saida.println(
                                    "[FALHA] Saldo insuficiente."
                            );

                        } else {

                            // Atualiza o saldo após o saque
                            double novoSaldo = saldoAtual - valor;

                            contas.put(numeroConta, novoSaldo);

                            saida.println("[SUCESSO] Saque realizado.");
                        }

                    } catch (NumberFormatException e) {

                        saida.println(
                                "[FALHA] Valor inválido."
                        );
                    }

                // =========================
                // COMANDO DESCONHECIDO
                // =========================
                } else {

                    saida.println(
                            "[FALHA] Comando desconhecido."
                    );
                }
            }

            // Fecha a conexão com o cliente
            cliente.close();

            // Fecha o servidor
            servidor.close();

            System.out.println("Servidor encerrado.");

        } catch (IOException e) {

            // Exibe uma mensagem caso ocorra algum erro
            System.out.println(
                    "Erro no servidor: " + e.getMessage()
            );
        }
    }

    static String formatarValor(double valor) {

        return String.format(
                new Locale("pt", "BR"),
                "R$ %.2f",
                valor
        );
    }
}
