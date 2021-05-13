package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;



public class SelectMarblesResponseMessage extends GameMessage {
    private final String rowOrColumn;
    private int index;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SelectMarblesResponseMessage(String rowOrColumn, int index) {
        this.rowOrColumn = rowOrColumn;
        this.index = index;
    }
    public String getRowOrColumn() {
        return rowOrColumn;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler) {
        handler.handle(this);
    }

}
