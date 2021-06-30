package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.utils.GameUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class, it listens for new clients trying  to connect and keeps the list of the rooms and clients.
 */
public class Server {

    private final int PORT = 31415;
    private final ServerSocket serverSocket;
    List<ClientConnection> pendingConnections = new ArrayList<>();
    private final ServerController serverController=new ServerController(this);
    private final Map<Integer , Room> rooms = new HashMap<>();

    /**
     * Constructor of the Server class.
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);

    }

    /**
     * Gets the server controller.
     * @return serverController.
     */
    public ServerController getServerController() {
        return serverController;
    }

    /**
     * Gets the list of the connections that haven't been assigned to a room yet.
     * @return pendingConnections.
     */
    public List<ClientConnection> getPendingConnections() {
        return pendingConnections;
    }

    public void addRoom(int roomId, Room room){
        rooms.put(roomId,room);
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public void terminateRoom(int roomID) {
        System.out.println("Deleting room " + roomID);
        rooms.remove(roomID);
    }

    /**
     * Listens for new clients.
     */
    public void run() {
        while (true) {
            System.out.println("waiting for client connection...");
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("new client connected");
                ClientConnection clientConnection = new ClientConnection(clientSocket, this);

                ExecutorService executor = Executors.newCachedThreadPool();

                pendingConnections.add(clientConnection);
                executor.submit(clientConnection);
                executor.submit(clientConnection::checkConnection);
            } catch (IOException e) {
                System.out.println("Connection error");
            }
        }
    }
}
