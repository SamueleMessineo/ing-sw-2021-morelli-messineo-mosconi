package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;

public class Room {
    private final Game game;
    private final int numberOfPlayers;
    private final boolean isPrivate;
    private ArrayList<ClientConnection> connections = new ArrayList<>();

    public Room(Game game, int numberOfPlayers, boolean isPrivate, ClientConnection host) {
        this.game = game;
        this.numberOfPlayers = numberOfPlayers;
        this.isPrivate = isPrivate;
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
        for (ClientConnection connection:
             connections) {
            connection.sendMessage(m);

        }
    }
}
