package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.model.Game;

public class Room {
    private final Game game;
    private final int numberOfPlayers;
    private final boolean isPrivate;

    public Room(Game game, int numberOfPlayers, boolean isPrivate) {
        this.game = game;
        this.numberOfPlayers = numberOfPlayers;
        this.isPrivate = isPrivate;
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
}
