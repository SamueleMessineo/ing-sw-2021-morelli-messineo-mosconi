package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;

/**
 * Message with the chosen leader to play.
 */
public class PlayLeaderResponseMessage extends GameMessage{
    private final int cardIndex;

    public PlayLeaderResponseMessage(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public int getCardIndex() {
        return cardIndex;
    }


    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler) {
        handler.handle(this);
    }
}
