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
        Room room =  new Room(new Game(), numberOfPlayers, privateRoom, clientConnection);
        room.getGame().addPlayer(username);

        server.addRoom(roomId,room);

        System.out.println("room creata");
    }

    public void addPlayerByRoomId(String username,String roomId){
        if(server.getRooms().get(roomId).getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
            server.getRooms().get(roomId).getGame().addPlayer(username);
        }

    }

    public void addPlayerToPublicRoom(int numberOfPlayers, String username){

        List<Room> rooms = new ArrayList<>(server.getRooms().values());
        System.out.println(rooms.size());
        Room room = rooms.stream().filter(room1 -> room1.getNumberOfPlayers() == numberOfPlayers).findAny().get();
        if(room.getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
            room.getGame().addPlayer(username);
        }


    }
}
