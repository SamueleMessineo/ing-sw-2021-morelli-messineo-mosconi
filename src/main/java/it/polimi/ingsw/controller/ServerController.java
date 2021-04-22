package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.RoomDetailsMessage;
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

        sendRoomDetails(roomId, room, clientConnection);

    }

    public void addPlayerByRoomId(String username,String roomId, ClientConnection clientConnection){
        if(server.getRooms().get(roomId).getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
            server.getRooms().get(roomId).getGame().addPlayer(username);
        }

        sendRoomDetails(roomId, server.getRooms().get(roomId), clientConnection);


    }

    public void addPlayerToPublicRoom(int numberOfPlayers, String username, ClientConnection clientConnection){

        List<Room> rooms = new ArrayList<>(server.getRooms().values());
        System.out.println(rooms.size());
        Room room = rooms.stream().filter(room1 -> room1.getNumberOfPlayers() == numberOfPlayers).findAny().get();
        if(room.getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
            room.getGame().addPlayer(username);
        }
        sendRoomDetails(server.getRooms().entrySet().stream().filter(room1 -> room1.equals(room)).iterator().next().getKey(), room, clientConnection);

    }

    public void sendRoomDetails(String roomId, Room room, ClientConnection clientConnection){
        ArrayList<String> players = new ArrayList<>();
        for (Player player:
                room.getGame().getPlayers()) {
            players.add(player.getUsername());

        }
        clientConnection.sendMessage(new RoomDetailsMessage(players, room.getNumberOfPlayers(), roomId));
    }
}
