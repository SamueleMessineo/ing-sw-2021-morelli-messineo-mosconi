package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;

public class SelectStackToPlaceCardResponseMessage extends GameMessage{
    private final int selectedStackIndex;

    public SelectStackToPlaceCardResponseMessage(int selectedStackIndex) {
        this.selectedStackIndex = selectedStackIndex;
    }

    public int getSelectedStackIndex() {
        return selectedStackIndex;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
