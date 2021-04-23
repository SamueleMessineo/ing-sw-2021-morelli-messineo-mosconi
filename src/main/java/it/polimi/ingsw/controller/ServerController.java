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
    private int roomId = 999;

    public ServerController(Server server) {
        this.server = server;
    }

    public void createRoom(boolean privateRoom, String username, int numberOfPlayers, ClientConnection clientConnection){
        List<ClientConnection> clientConnections=server.getPendingConnections();
        clientConnections.remove(clientConnection);


        Room room =  new Room(new Game(), numberOfPlayers, privateRoom, clientConnection);
        room.getGame().addPlayer(username);

        int currentRoomId = getRoomId();
        server.addRoom(currentRoomId,room);

        sendRoomDetails(currentRoomId, room, clientConnection);

    }

    public void addPlayerByRoomId(String username,int roomId, ClientConnection clientConnection){
        if (server.getRooms().get(roomId) != null){
            if(server.getRooms().get(roomId).getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
                server.getRooms().get(roomId).getGame().addPlayer(username);
                List<ClientConnection> clientConnections=server.getPendingConnections();
                clientConnections.remove(clientConnection);
                server.getRooms().get(roomId).addConnection(clientConnection);
            } //TODO else KO message

            sendRoomDetails(roomId, server.getRooms().get(roomId), clientConnection);
        } //TODO else KO message



    }

    public void addPlayerToPublicRoom(int numberOfPlayers, String username, ClientConnection clientConnection){

        List<Room> rooms = new ArrayList<>(server.getRooms().values());
        System.out.println(rooms.size());
        Room room = rooms.stream().filter(room1 -> room1.getNumberOfPlayers() == numberOfPlayers && !room1.isPrivate()).findAny().orElseThrow();
        System.out.println(room);
        //TODO gestire bene il caso in cui non ci siano room con quel numero di giocatori;
        //si potrebbe chiamare il metodo createRoom passando questo user
        if(room.getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
            System.out.println("valid username");
            room.getGame().addPlayer(username);
            List<ClientConnection> clientConnections = server.getPendingConnections();
            clientConnections.remove(clientConnection);
            room.addConnection(clientConnection);
            int roomId = 1;
            sendRoomDetails(1000, room, clientConnection);
        }


    }

    public void sendRoomDetails(int roomId, Room room, ClientConnection clientConnection){
        System.out.println("send details");
        System.out.println(roomId);
        System.out.println(room);
        ArrayList<String> players = new ArrayList<>();
        for (Player player:
                room.getGame().getPlayers()) {
            players.add(player.getUsername());

        }
        room.sendAll(new RoomDetailsMessage(players, room.getNumberOfPlayers(), roomId));
    }

    private int getRoomId(){
        roomId++;
        if (roomId > 9999)roomId=1000;
        return roomId;
    }
}
