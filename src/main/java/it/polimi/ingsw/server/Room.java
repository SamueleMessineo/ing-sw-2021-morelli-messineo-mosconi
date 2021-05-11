package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Message;

import java.util.ArrayList;

public class Room {
    private final Game game;
    private final int numberOfPlayers;
    private final boolean isPrivate;
    private ArrayList<ClientConnection> connections = new ArrayList<>();
    private ClassicGameController gameController;
    private Turn currentTurn;
    private final int id;

    public Room(Game game, int numberOfPlayers, boolean isPrivate, ClientConnection host, int id) {
        this.game = game;
        this.numberOfPlayers = numberOfPlayers;
        this.isPrivate = isPrivate;
        this.id = id;
        addConnection(host);
    }

    public Game getGame() {
        return game;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isFull() {
        return connections.size() >= numberOfPlayers;
    }

    public void addConnection(ClientConnection clientConnection){
        if (connections.size() < numberOfPlayers){
            connections.add(clientConnection);
        }
    }

    public void sendAll(Message m){
        for (ClientConnection connection : connections) {
            if (connection.isConnected())
                connection.sendMessage(m);
        }
    }

    public void setGameController(ClassicGameController gameController) {
        this.gameController = gameController;
    }

    public ArrayList<ClientConnection> getConnections() {
        return connections;
    }

    public Player getPlayerFromConnection(ClientConnection clientConnection){
        return getGame().getPlayers().get(getConnections().indexOf(clientConnection));
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getId() {
        return id;
    }

}
