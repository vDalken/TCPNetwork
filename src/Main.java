import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

final class Main {
    /**
     * keyword to disconnect from the network.
     */
    private static final String LEAVING_NETWORK_KEYWORD = "sair";
    /**
     * port number of the server socket.
     */
    private static final int PORT_NUMBER = 9806;

    private Main() {
    }

    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("network is waiting for any connection");
            Socket socket = serverSocket.accept();
            System.out.println("Connection established");
            String receivedMessage;
            String messageToSend;
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
                boolean isTerminalPersonTryingToLeave;
                boolean isConsolePersonTryingToLeave=false;
                do {
                    receivedMessage = bufferedReader.readLine();
                    System.out.println("The person on the other end: " + receivedMessage);
                    isTerminalPersonTryingToLeave = receivedMessage.trim().equalsIgnoreCase(LEAVING_NETWORK_KEYWORD);
                    if (isTerminalPersonTryingToLeave) break;
                    messageToSend = scanner.nextLine();
                    output.println("The person on the other end: " + messageToSend);
                    isConsolePersonTryingToLeave = messageToSend.trim().equalsIgnoreCase(LEAVING_NETWORK_KEYWORD);
                } while (!isConsolePersonTryingToLeave);

                if (isConsolePersonTryingToLeave) {
                    output.println("The person on the other end disconnected");
                    System.out.println("You disconnected from the network");
                } else {
                    System.out.println("The person on the other end disconnected");
                    output.println("You disconnected from the network");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
