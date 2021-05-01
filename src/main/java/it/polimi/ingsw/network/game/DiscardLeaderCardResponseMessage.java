package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;

public class DiscardLeaderCardResponseMessage extends GameMessage {
    private final int card;

    public DiscardLeaderCardResponseMessage(int card) {
        this.card = card;
    }

    public int getCard() {
        return card;
    }


    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
