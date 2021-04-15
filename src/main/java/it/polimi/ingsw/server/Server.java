package it.polimi.ingsw.server;

import it.polimi.ingsw.network.GameMessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int PORT = 31415;
    private final ServerSocket serverSocket;
    List<ClientConnection> pendingConnections = new ArrayList<>();

    private final GameMessageHandler handler = new GameMessageHandler();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run() {
        while (true) {
            System.out.println("waiting for client connection");
            try {
                Socket clientSocket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(clientSocket, this);

                ExecutorService executor = Executors.newCachedThreadPool();

                pendingConnections.add(clientConnection);
                executor.submit(clientConnection);

            } catch (IOException e) {
                System.out.println("Connection error");
            }
        }
    }
}
