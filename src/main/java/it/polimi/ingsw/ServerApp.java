package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.IOException;

/**
 * Server executable class.
 */
public class ServerApp {
    /**
     * Starts the Server.
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }
}
