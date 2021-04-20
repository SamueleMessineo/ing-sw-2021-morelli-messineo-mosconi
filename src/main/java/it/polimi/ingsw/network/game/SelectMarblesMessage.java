package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;

enum RowOrColumn{
    ROW, COLUMN;
}

public class SelectMarblesMessage extends GameMessage {
    private RowOrColumn rowOrColumn;
    private int index;
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SelectMarblesMessage(RowOrColumn rowOrColumn, int index) {
        this.rowOrColumn = rowOrColumn;
        this.index = index;
    }
    public RowOrColumn getRowOrColumn() {
        return rowOrColumn;
    }

    public void setRowOrColumn(RowOrColumn rowOrColumn) {
        this.rowOrColumn = rowOrColumn;
    }

    @Override
    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
