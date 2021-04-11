package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.controller.MessageHandler;

public class SelectCardMessage implements Message {

    private int num = 1;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public void accept(MessageHandler handler) {
        handler.handle(this);
    }
}
