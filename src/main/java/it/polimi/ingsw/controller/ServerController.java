package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;

import java.util.List;

public class ServerController {
    private final Server server;

    public ServerController(Server server) {
        this.server = server;
    }

    public void createRoom(boolean privateRoom, String username, int numberOfPlayers, ClientConnection clientConnection){
        List<ClientConnection> clientConnections=server.getPendingConnections();
        clientConnections.remove(clientConnection);
    }
}
