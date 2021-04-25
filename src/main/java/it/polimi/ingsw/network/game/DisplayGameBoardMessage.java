package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;

public class DisplayGameBoardMessage extends GameMessage {

    private final String currentPlayer;

    public DisplayGameBoardMessage(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
