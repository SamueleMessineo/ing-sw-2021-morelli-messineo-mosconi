package it.polimi.ingsw.network.messages.game;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.controller.MessageHandler;

public class EndTurnMessage implements Message {

    private String name = "bob";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(MessageHandler handler) {
        handler.handle(this);
    }
}
