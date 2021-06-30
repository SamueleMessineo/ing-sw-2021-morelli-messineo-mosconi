package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.Message;

import java.util.ArrayList;

/**
 * This class contains all the network information of game plus the controller and current turn state.
 */
public class Room {
    private final Game game;
    private final int numberOfPlayers;
    private final boolean isPrivate;
    private ArrayList<ClientConnection> connections = new ArrayList<>();
    private ClassicGameController gameController;
    private Turn currentTurn;
    private final int id;
    private boolean recreated;

    /**
     * Constructor of the class room.
     */
    public Room(Game game, int numberOfPlayers, boolean isPrivate, ClientConnection host, int id) {
        this.game = game;
        this.numberOfPlayers = numberOfPlayers;
        this.isPrivate = isPrivate;
        this.id = id;
        addConnection(host);
        recreated = false;
    }

    /**
     * Gets the game
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the number of players of the game.
     * @return numberOfPlayers
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Gets if the game is private or public.
     * @return isPrivate
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * True if the room players equals to the number of players the game where created for,
     * @return true if the room is full.
     */
    public boolean isFull() {
        return connections.size() >= numberOfPlayers;
    }

    /**
     * Adds a ClientConnection to connections list.
     * @param clientConnection a new clientConnection.
     */
    public void addConnection(ClientConnection clientConnection){
        if (connections.size() < numberOfPlayers){
            connections.add(clientConnection);
        }
    }

    /**
     * Sends a message to all the connected players.
     * @param m the message to send.
     */
    public void sendAll(Message m){
        for (ClientConnection connection : connections) {
            if (connection.isConnected())
                connection.sendMessage(m);
        }
    }

    /**
     * Sets the gameController.
     * @param gameController the gameController of the current game.
     */
    public void setGameController(ClassicGameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Gets the list of the clientConnections.
     * @return connections
     */
    public ArrayList<ClientConnection> getConnections() {
        return connections;
    }

    /**
     * Gets the player associated to a certain connection.
     * @param clientConnection the clientConnection of the player of interest.
     * @return the Player associated to clientConnection.
     */
    public Player getPlayerFromConnection(ClientConnection clientConnection){
        return getGame().getActivePlayers().get(getConnections().indexOf(clientConnection));
    }

    /**
     * Gets the current turn.
     * @return currentTurn.
     */
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets the currentTurn.
     * @param currentTurn an instance of Turn.
     */
    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    /**
     * Gets the id of the room (a number from 1000-9999).
     * @return id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the game controller.
     * @return gameController.
     */
    public ClassicGameController getGameController() {
        return gameController;
    }

    /**
     * Sets recreated to true if the game was reloaded after the server had gone down.
     * @param recreated true if the game was reloaded after the server had gone down.
     */
    public void setRecreated(boolean recreated) {
        this.recreated = recreated;
    }

    /**
     * Gets true if the game was reloaded after the server had gone down.
     * @return true if the game was reloaded after the server had gone down.
     */
    public boolean isRecreated() {
        return recreated;
    }
}
