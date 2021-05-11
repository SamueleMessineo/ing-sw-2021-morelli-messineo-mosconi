package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;

public class BuyDevelopmentCardResponseMessage extends GameMessage{
    private final int selectedCardIndex;

    public BuyDevelopmentCardResponseMessage(int selectedCardIndex) {
        this.selectedCardIndex = selectedCardIndex;
    }

    public int getSelectedCardIndex() {
        return selectedCardIndex;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }

    public void accept(LocalMessageHandler handler) {
        handler.handle(this);
    }
}
