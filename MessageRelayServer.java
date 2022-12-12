import java.io.*;
import java.net.*;
import java.util.*;

public class MessageRelayServer {
  public static void main(String[] args) throws IOException {
    // Create a new server socket on localhost port 1234
    ServerSocket serverSocket = new ServerSocket(1234);
    System.out.println("MessageRelayServer started on localhost:1234");

    // Create a list to store connected clients
    List<Socket> clients = new ArrayList<>();

    // Continuously accept new client connections
    while (true) {
      // Accept a new client connection
      Socket client = serverSocket.accept();

      // Add the new client to the list of connected clients
      clients.add(client);

      // Print a message to the console
      System.out.println("New client connected: " + client.getRemoteSocketAddress());

      // Create a new thread to handle communication with the client
      new Thread(() -> {
        try {
          // Create input and output streams for the client
          BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
          // PrintWriter out = new PrintWriter(client.getOutputStream(), true); -> This was sending it to itself 

          // Continuously read messages from the client
          while (true) {
            // Read a message from the client
            String message = in.readLine();

            // Print the message to the console
            System.out.println("Received message from " + client.getRemoteSocketAddress() + ": " + message);

            // Send the message to all other clients, excluding the client that sent the message
            for (Socket otherClient : clients) {
              if (!otherClient.getRemoteSocketAddress().equals(client.getRemoteSocketAddress())) {
                //Added: -> This now allow to write to other clients 
                PrintWriter otherClientOut = new PrintWriter(otherClient.getOutputStream(), true);
                otherClientOut.println(message);
  
              }
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }).start();
    }
  }
}
