import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        // Scanner usado para receber as configurações digitadas pelo usuário
        Scanner scanner = new Scanner(System.in);

        // Solicita o endereço IP e a porta do servidor
        System.out.print("Digite o IP do servidor: ");
        String ip = scanner.nextLine();

        System.out.print("Digite a porta: ");
        int porta = scanner.nextInt();

        try {
            // Cria uma conexão TCP com o servidor
            Socket socket = new Socket(ip, porta);

            System.out.println("Conectado ao servidor!");
            System.out.println("IP do servidor: " + ip);
            System.out.println("Porta: " + porta);

        } catch (IOException e) {
            // Exibe uma mensagem caso não seja possível conectar
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
