package it.polimi.ingsw.network.game;

import it.polimi.ingsw.network.GameMessageHandler;

public class EndTurnMessage extends GameMessage {

    private String name = "bob";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
