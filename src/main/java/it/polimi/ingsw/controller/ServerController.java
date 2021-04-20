package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;

import java.util.List;
import java.util.UUID;

public class ServerController {
    private final Server server;

    public ServerController(Server server) {
        this.server = server;
    }

    public void createRoom(boolean privateRoom, String username, int numberOfPlayers, ClientConnection clientConnection){
        List<ClientConnection> clientConnections=server.getPendingConnections();
        clientConnections.remove(clientConnection);

        String roomId=UUID.randomUUID().toString();
        Game game=new Game();
        game.addPlayer(username);

        server.addRoom(roomId,game);
    }

    public void addPlayerByRoomId(String username,String roomId){
        Game game=server.getRooms().get(roomId);
        game.addPlayer(username);
    }
}
