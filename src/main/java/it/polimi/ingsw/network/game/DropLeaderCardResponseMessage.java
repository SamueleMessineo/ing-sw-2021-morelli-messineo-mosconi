package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;

public class DropLeaderCardResponseMessage extends GameMessage {
    private final int card;

    public DropLeaderCardResponseMessage(int card) {
        this.card = card;
    }

    public int getCard() {
        return card;
    }


    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler){handler.handle(this);}
}
