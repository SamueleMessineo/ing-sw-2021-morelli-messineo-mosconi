package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;

public class SelectMoveResponseMessage extends GameMessage{
    private final String move;

    public SelectMoveResponseMessage(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler){handler.handle(this);}

}
