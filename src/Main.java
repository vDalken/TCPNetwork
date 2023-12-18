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
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                performNetworkCommunication(bufferedReader, output, scanner);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void performNetworkCommunication(final BufferedReader bufferedReader, final PrintWriter output, final Scanner scanner)
            throws IOException {
        boolean terminalLeaving;
        boolean consoleLeaving = false;
        String receivedMessage;
        String messageToSend;

        do {
            receivedMessage = readMessage(bufferedReader);
            System.out.println("The person on the other end: " + receivedMessage);

            terminalLeaving = receivedMessage.trim().equalsIgnoreCase(LEAVING_NETWORK_KEYWORD);
            if (terminalLeaving) {
                break;
            }

            messageToSend = getMessageFromConsole(scanner);
            sendMessage(output, "The person on the other end: " + messageToSend);

            consoleLeaving = messageToSend.trim().equalsIgnoreCase(LEAVING_NETWORK_KEYWORD);
        } while (!consoleLeaving);

        processDisconnection(consoleLeaving, output);
    }

    private static String readMessage(final BufferedReader bufferedReader) throws IOException {
        return bufferedReader.readLine();
    }

    private static String getMessageFromConsole(final Scanner scanner) {
        return scanner.nextLine();
    }

    private static void sendMessage(final PrintWriter output, final String message) {
        output.println(message);
    }

    private static void processDisconnection(final boolean consoleLeaving, final PrintWriter output) {
        if (consoleLeaving) {
            sendMessage(output, "The person on the other end disconnected");
            System.out.println("You disconnected from the network");
        } else {
            System.out.println("The person on the other end disconnected");
            sendMessage(output, "You disconnected from the network");
        }
    }
}
