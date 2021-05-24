package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.server.Room;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.GameUtils;

import java.util.*;

public class ServerController {
    private final Server server;
    private int roomId = 999;

    public ServerController(Server server) {
        this.server = server;
    }

    public void createRoom(boolean privateRoom, String username, int numberOfPlayers, ClientConnection clientConnection){
        List<ClientConnection> clientConnections=server.getPendingConnections();
        clientConnections.remove(clientConnection);

        int currentRoomId = getRoomId();

        Room room =  new Room(new Game(), numberOfPlayers, privateRoom, clientConnection, currentRoomId);
        room.getGame().addPlayer(username);

        server.addRoom(currentRoomId,room);

        sendRoomDetails(currentRoomId, room, clientConnection);

        if (room.isFull()){
            startSoloGame(room);
        }
    }

    public void addPlayerByRoomId(String username,int roomId, ClientConnection clientConnection){
        if (server.getRooms().get(roomId) == null) {
            clientConnection.sendMessage(new ErrorMessage("room not found"));
            return;
        }
        Room room = server.getRooms().get(roomId);
        //server.getRooms().get(roomId).getGame().getPlayers().stream().anyMatch(player -> player.getUsername().equals(username))
        if (!room.isFull() && room.getGame().getPlayerByUsername(username)!=null) {
            clientConnection.sendMessage(new ErrorMessage("username is taken"));
            return;
        }

        if (room.isFull() && room.getGame().getPlayerByUsername(username)==null) {
            clientConnection.sendMessage(new ErrorMessage("room is full"));
            return;
        }
        //to handle reconnection
        if(room.isFull() && room.getGame().getPlayerByUsername(username)!=null){
           if(room.getGame().getPlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
               room.getGame().getPlayerByUsername(username).setActive(true);

               clientConnection.setGameMessageHandler((room.getConnections().get(room.getGame().getPlayers().indexOf(room.getGame().getPlayerByUsername(username))).getGameMessageHandler()));
               clientConnection.getGameMessageHandler().setClientConnection(clientConnection);
               room.getConnections().remove(room.getGame().getPlayers().indexOf(room.getGame().getPlayerByUsername(username)));
               room.getConnections().add(room.getGame().getPlayers().indexOf(room.getGame().getPlayerByUsername(username)), clientConnection);
               List<ClientConnection> clientConnections=server.getPendingConnections();
               clientConnections.remove(clientConnection);
               if(!clientConnection.getGameMessageHandler().isReady()){
                   clientConnection.getGameMessageHandler().initialSelections();
               }
               else {
                   room.sendAll(new StringMessage(username + " is back in the game!"));
                   room.sendAll(new UpdateGameStateMessage(room.getGame()));
                   clientConnection.sendMessage(new UpdateAndDisplayGameStateMessage(room.getGame()));
                   if(room.getGame().getPlayers().size()==1){
                       room.setCurrentTurn(new Turn(username, room.getGameController().computeNextPossibleMoves(false)));
                       room.sendAll(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
                   }
               }

               return;
           }
           else {
               clientConnection.sendMessage(new ErrorMessage("room is full"));
               return;
           }
        }
        room.getGame().addPlayer(username);
        List<ClientConnection> clientConnections=server.getPendingConnections();
        clientConnections.remove(clientConnection);
        room.addConnection(clientConnection);
        sendRoomDetails(roomId, room, clientConnection);
        if (room.isFull()){
            startGame(room);
        }
    }

    public void addPlayerToPublicRoom(int numberOfPlayers, String username, ClientConnection clientConnection){

        List<Room> rooms = new ArrayList<>(server.getRooms().values());

        if (numberOfPlayers == 0) {
            Random r = new Random();
            numberOfPlayers = r.nextInt(4)+1;
        }
        Room room;
        try {
            int finalNumberOfPlayers = numberOfPlayers;
            room = rooms.stream().filter(room1 -> room1.getNumberOfPlayers() == finalNumberOfPlayers && !room1.isPrivate()).findAny().orElseThrow();
        } catch (NoSuchElementException e) {
            createRoom(false, username, numberOfPlayers, clientConnection);
            return;
        }

        if(room.getGame().getPlayers().stream().anyMatch(player -> player.getUsername().equals(username))){
            clientConnection.sendMessage(new ErrorMessage("username is taken"));
            return;
        }

        room.getGame().addPlayer(username);
        List<ClientConnection> clientConnections = server.getPendingConnections();
        clientConnections.remove(clientConnection);
        room.addConnection(clientConnection);

        int currentRoomId = -1;
        for (Map.Entry<Integer, Room> entry: server.getRooms().entrySet())
        {
            if (room.equals(entry.getValue())) {
                currentRoomId = entry.getKey();
            }
        }
        sendRoomDetails(currentRoomId, room, clientConnection);

        if (room.isFull()){
            startGame(room);
        }
    }

    public void sendRoomDetails(int roomId, Room room, ClientConnection clientConnection){
        ArrayList<String> players = new ArrayList<>();
        for (Player player : room.getGame().getPlayers()) {
            players.add(player.getUsername());
        }
        room.sendAll(new RoomDetailsMessage(players, room.getNumberOfPlayers(), roomId));
    }

    private int getRoomId(){
        roomId++;
        if (roomId > 9999)roomId=1000;
        return roomId;
    }

    private void startGame(Room room){
        room.sendAll(new StringMessage("Game is starting!"));
        ClassicGameController classicGameController = new ClassicGameController(room);
        classicGameController.startGame();
        room.setGameController(classicGameController);
        for (ClientConnection client:
             room.getConnections()) {
            client.setGameMessageHandler(new GameMessageHandler(classicGameController, client, room));
            client.getGameMessageHandler().initialSelections();
        }
    }

    private void startSoloGame(Room room){
        ClientConnection clientConnection = room.getConnections().get(0);
        ClassicGameController soloGameController = new SoloGameController(room);
        room.setGameController(soloGameController);
        clientConnection.setGameMessageHandler(new GameMessageHandler(soloGameController, clientConnection, room));
        soloGameController.startGame();
        clientConnection.getGameMessageHandler().initialSelections();
    }
}
