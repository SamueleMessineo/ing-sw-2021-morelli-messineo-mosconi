package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;

public class DropInitialLeaderCardsResponseMessage extends GameMessage {
    private final int card1;
    private final int card2;

    public DropInitialLeaderCardsResponseMessage(int card1, int card2) {
        this.card1 = card1;
        this.card2 = card2;
    }

    public int getCard1() {
        return card1;
    }

    public int getCard2() {
        return card2;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }

    public void accept(LocalMessageHandler handler){handler.handle(this);}
}
