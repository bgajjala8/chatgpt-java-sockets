import java.io.*;
import java.net.*;
import java.util.logging.*;

public class MessageRelayClient {
    private static final Logger logger = Logger.getLogger(MessageRelayClient.class.getName());

    public static void main(String[] args) throws IOException {
        // Create a socket to connect to the server
        Socket socket = new Socket("localhost", 1234);
        
        // Create input and output streams to read from and write to the server
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Listen for messages from the server in a separate thread
        Thread listener = new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    logger.info("Received message from server: " + response);
                    System.out.println(response);
                }
            } catch (IOException e) {
                logger.severe("Error reading from server: " + e.getMessage());
            }
        });
        listener.start();

        // Read input from the CLI and write it to the server
        BufferedReader cli = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = cli.readLine()) != null) {
            out.println(input);
            logger.info("Sent message to server: " + input);
        }

        // Close the socket when done
        socket.close();
    }
}

