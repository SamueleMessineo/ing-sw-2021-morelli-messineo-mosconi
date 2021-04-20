package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.setup.Room;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;

import java.util.ArrayList;
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
        Room room =  new Room(new Game(), numberOfPlayers, privateRoom);
        room.getGame().addPlayer(username);

        server.addRoom(roomId,room);
    }

    public void addPlayerByRoomId(String username,String roomId){
        server.getRooms().get(roomId).getGame().addPlayer(username);
    }

    public void addPlayerToPublicRoom(int numberOfPlayers, String username){
        List<Room> rooms = new ArrayList<>(server.getRooms().values());
        Room room = rooms.stream().filter(room1 -> room1.getNumberOfPlayers() == numberOfPlayers).findAny().get();
        room.getGame().addPlayer(username);
    }
}
