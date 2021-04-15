package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;

public class SelectCardMessage extends GameMessage {

    private int num = 1;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
