import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Locale;

public class Servidor {

    static Map<String, Double> contas = new HashMap<>();

    static final String PASTA_CONTAS = "contas";

    public static void main(String[] args) {

        // Scanner usado para receber as configurações digitadas pelo usuário
        Scanner scanner = new Scanner(System.in);

        // Solicita o endereço IP e a porta
        System.out.print("Digite o IP do servidor: ");
        String ip = scanner.nextLine();

        System.out.print("Digite a porta: ");
        int porta = scanner.nextInt();

        carregarContas();

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

            while ((mensagem = entrada.readLine()) != null) {

                System.out.println("Comando recebido: " + mensagem);

                // Encerra a conexão quando o cliente enviar /exit
                if (mensagem.equalsIgnoreCase("/exit")) {
                    saida.println("[SUCESSO] Conexão encerrada.");
                    break;
                }

                String[] partes = mensagem.trim().split("\\s+");

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

                        // Obtém o saldo atual e atualiza
                        double saldoAtual = contas.get(numeroConta);

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

            // Salva todas as contas antes de encerrar o servidor
            salvarContas();

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

    // Formata os valores monetários com duas casas decimais
    static String formatarValor(double valor) {

        return String.format(
                new Locale("pt", "BR"),
                "R$ %.2f",
                valor
        );
    }

    // Carrega as contas salvas em arquivos ao iniciar o servidor
    static void carregarContas() {

        File pasta = new File(PASTA_CONTAS);

        if (!pasta.exists()) {
            pasta.mkdirs();
            return;
        }

        // Obtém os arquivos de contas existentes
        File[] arquivos = pasta.listFiles(
                (diretorio, nome) ->
                        nome.startsWith("conta_") && nome.endsWith(".txt")
        );

        // Se não houver arquivos, não há contas para carregar
        if (arquivos == null) {
            return;
        }

        // Percorre todos os arquivos de contas
        for (File arquivo : arquivos) {

            try (BufferedReader leitor =
                         new BufferedReader(new FileReader(arquivo))) {

                String linhaConta = leitor.readLine();

                String linhaSaldo = leitor.readLine();

                if (linhaConta != null && linhaSaldo != null) {

                    String numeroConta =
                            linhaConta.replace("Número da conta: ", "").trim();

                    String valorSaldo =
                            linhaSaldo.replace("Saldo: R$ ", "").trim();

                    // Converte a vírgula decimal para ponto
                    valorSaldo = valorSaldo.replace(",", ".");

                    double saldo = Double.parseDouble(valorSaldo);

                    // Adiciona a conta ao mapa
                    contas.put(numeroConta, saldo);
                }

            } catch (IOException | NumberFormatException e) {

                System.out.println(
                        "[FALHA] Não foi possível carregar a conta: "
                        + arquivo.getName()
                );
            }
        }

        System.out.println(
                "[SUCESSO] Contas carregadas: "
                + contas.size()
        );
    }

    // Salva todas as contas em arquivos individuais
    static void salvarContas() {

        File pasta = new File(PASTA_CONTAS);

        // Cria a pasta caso ela ainda não exista
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        // Percorre todas as contas armazenadas
        for (Map.Entry<String, Double> conta : contas.entrySet()) {

            String numeroConta = conta.getKey();
            double saldo = conta.getValue();

            // Cria um arquivo para cada conta
            File arquivo = new File(
                    pasta,
                    "conta_" + numeroConta + ".txt"
            );

            try (PrintWriter escritor =
                         new PrintWriter(new FileWriter(arquivo))) {

                // Salva o número da conta de forma organizada
                escritor.println(
                        "Número da conta: " + numeroConta
                );

                escritor.println(
                        "Saldo: " + formatarValor(saldo)
                );

            } catch (IOException e) {

                System.out.println(
                        "[FALHA] Não foi possível salvar a conta: "
                        + numeroConta
                );
            }
        }

        System.out.println(
                "[SUCESSO] Contas salvas: "
                + contas.size()
        );
    }
}
