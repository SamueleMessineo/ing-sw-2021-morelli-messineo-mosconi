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

/**
 * ServerController class, handles the creation of the rooms for the game.
 */
public class ServerController {
    private final Server server;
    private int roomId = 999;

    /**
     * ServerController class constructor
     */
    public ServerController(Server server) {
        this.server = server;
    }

    /**
     * Creates a room for a game
     * @param privateRoom true if it is a private game
     * @param username of the player creating the room
     * @param numberOfPlayers of the game
     * @param clientConnection of the player creating the room
     */
    public synchronized void createRoom(boolean privateRoom, String username, int numberOfPlayers, ClientConnection clientConnection){
        List<ClientConnection> clientConnections=server.getPendingConnections();
        clientConnections.remove(clientConnection);

        int currentRoomId = getRoomId();
        while (GameUtils.readGame(currentRoomId)!=null)currentRoomId=getRoomId();//checks if the game does not already exists
        Room room =  new Room(new Game(), numberOfPlayers, privateRoom, clientConnection, currentRoomId);
        room.getGame().addPlayer(username);

        server.addRoom(currentRoomId,room);

        sendRoomDetails(currentRoomId, room);

        if (room.isFull()){
            startSoloGame(room);
        }
    }

    /**
     * Adds the player to the game that has the selected roomID
     * @param username of the player to add
     * @param roomId of the game the player wants to join
     * @param clientConnection of the player to add
     */
    public synchronized void addPlayerByRoomId(String username,int roomId, ClientConnection clientConnection){
        if (server.getRooms().get(roomId) == null) {
            Game game = GameUtils.readGame(roomId);
            if(game!=null && game.getPlayerByUsername(username) != null){
                //handling server persistence
                System.out.println("server persistence");
                ClassicGameController gameController;
                System.out.println(game.getPlayers());
                int playerNumber = game.getPlayers().size() ;
                Room room = new Room(game,playerNumber,true, null, roomId);
                room.setRecreated(true);

                if(game.getPlayers().size()==1){
                    gameController = new SoloGameController(room);
                } else gameController = new ClassicGameController(room);
                room.setGameController(gameController);
                server.addRoom(roomId, room);

                List<ClientConnection> clientConnections=server.getPendingConnections();
                clientConnections.remove(clientConnection);
                room.getConnections().remove(null);
                for (int i = 0; i < room.getNumberOfPlayers(); i++) {
                    System.out.println(i);
                    System.out.println(room.getConnections());
                    if(i!=game.getPlayers().indexOf(game.getPlayerByUsername(username))){
                        room.getConnections().add(i, null);
                    }else {
                        room.getConnections().add(game.getPlayers().indexOf(game.getPlayerByUsername(username)), clientConnection);
                    }
                }
                System.out.println(room.getConnections().size());

                clientConnection.setGameMessageHandler(new GameMessageHandler(gameController, clientConnection, room));
                for (Player player:
                        game.getPlayers()) {
                    System.out.println(player.getUsername());
                    if(!player.getUsername().equals(username))player.setActive(false);
                    else player.setActive(true);
                }

                if (room.getNumberOfPlayers() == game.getActivePlayers().size()){
                    room.setCurrentTurn(new Turn(username, room.getGameController().computeNextPossibleMoves(false)));
                    clientConnection.sendMessage(new UpdateAndDisplayGameStateMessage(game));
                    clientConnection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
                    return;
                }

                ArrayList<String> players = new ArrayList<>();
                for (Player p:
                     game.getActivePlayers()) {
                    players.add(p.getUsername());
                }
                room.getGameController().computeNextPossibleMoves(false);
                room.setCurrentTurn(new Turn(game.getCurrentPlayer().getUsername(), room.getGameController().computeNextPossibleMoves(false)));
                clientConnection.sendMessage(new RoomDetailsMessage(players, room.getNumberOfPlayers(), roomId));
                //sendRoomDetails(roomId, room);
                return;
            }
            clientConnection.sendMessage(new ErrorMessage("room not found"));
            return;
        }
        Room room = server.getRooms().get(roomId);
        //server.getRooms().get(roomId).getGame().getPlayers().stream().anyMatch(player -> player.getUsername().equals(username))
        if (!room.isFull()  && room.getGame().getPlayerByUsername(username)!=null && room.getGame().getPlayerByUsername(username).isActive()) {
            clientConnection.sendMessage(new ErrorMessage("username is taken"));
            return;
        }

        //case room is full
        if (room.isFull() && room.getGame().getPlayerByUsername(username)==null) {
            clientConnection.sendMessage(new ErrorMessage("room is full"));
            return;
        }
        //to handle reconnection
        if(room.isFull() && room.getGame().getPlayerByUsername(username)!=null){
           if(room.getGame().getActivePlayers().stream().noneMatch(player -> player.getUsername().equals(username))){
               room.getGame().getPlayerByUsername(username).setActive(true);

               clientConnection.setGameMessageHandler(new GameMessageHandler(room.getGameController(), clientConnection, room));
               room.getConnections().remove(room.getGame().getActivePlayers().indexOf(room.getGame().getPlayerByUsername(username)));
               room.getConnections().add(room.getGame().getActivePlayers().indexOf(room.getGame().getPlayerByUsername(username)), clientConnection);
               List<ClientConnection> clientConnections=server.getPendingConnections();
               clientConnections.remove(clientConnection);
               if(room.getGame().getPlayerByUsername(username).getLeaderCards().size()<=2){
                   clientConnection.getGameMessageHandler().setReady(true);
               }
               if(!clientConnection.getGameMessageHandler().isReady()){
                   clientConnection.getGameMessageHandler().initialSelections();
               }
               else {
                   room.sendAll(new StringMessage(username + " is back in the game!"));
                   room.sendAll(new UpdateGameStateMessage(room.getGame()));
                   if(room.isRecreated() ){
                       if( room.getGame().getActivePlayers().size() == room.getNumberOfPlayers()){
                           room.sendAll(new UpdateAndDisplayGameStateMessage(room.getGame()));
                           room.getConnections().get(room.getGame().getPlayers().indexOf(room.getGame().getCurrentPlayer())).sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
                           room.setRecreated(false);
                       }
                    } else clientConnection.sendMessage(new UpdateAndDisplayGameStateMessage(room.getGame()));

                   if(room.getGame().getActivePlayers().size()==1){
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
        sendRoomDetails(roomId, room);
        if (room.isFull()){
            startGame(room);
        }
    }

    /**
     * Adds the player to a game that has the selected number of player, if the number  is 0 the player will be added to a  random  game
     * @param numberOfPlayers of the game
     * @param username of the  player to add
     * @param clientConnection of the player to add
     */
    public synchronized void addPlayerToPublicRoom(int numberOfPlayers, String username, ClientConnection clientConnection){

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

        if(room.getGame().getActivePlayers().stream().anyMatch(player -> player.getUsername().equals(username))){
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
        sendRoomDetails(currentRoomId, room);

        if (room.isFull()){
            startGame(room);
        }
    }

    /**
     * Sends the players, number of players and roomID of the game to all players
     * @param roomId of the game
     * @param room of the game
     */
    public synchronized void sendRoomDetails(int roomId, Room room){
        ArrayList<String> players = new ArrayList<>();
        for (Player player : room.getGame().getActivePlayers()) {
            players.add(player.getUsername());
        }
        room.sendAll(new RoomDetailsMessage(players, room.getNumberOfPlayers(), roomId));
    }

    /**
     * Compute the id of the next room, it is an int between 1000 and 9999
     * @return the id of the next room
     */
    private synchronized int getRoomId(){
        roomId++;
        if (roomId > 9999)roomId=1000;
        return roomId;
    }

    /**
     * When the room is full sets the GameMessageHandler to all players and start the game
     * @param room of the game that is starting
     */
    private synchronized void startGame(Room room){
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

    /**
     * Starts a solo game
     * @param room of the game that is starting
     */
    private synchronized void startSoloGame(Room room){
        ClientConnection clientConnection = room.getConnections().get(0);
        ClassicGameController soloGameController = new SoloGameController(room);
        room.setGameController(soloGameController);
        clientConnection.setGameMessageHandler(new GameMessageHandler(soloGameController, clientConnection, room));
        soloGameController.startGame();
        clientConnection.getGameMessageHandler().initialSelections();
    }
}
