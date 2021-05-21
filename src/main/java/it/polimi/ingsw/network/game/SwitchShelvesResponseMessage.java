package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;

public class SwitchShelvesResponseMessage extends GameMessage {
    private final String shelf1;
    private final String shelf2;

    public SwitchShelvesResponseMessage(String shelf1, String shelf2) {
        this.shelf1 = shelf1;
        this.shelf2 = shelf2;
    }

    public String getShelf1() {
        return shelf1;
    }

    public String getShelf2() {
        return shelf2;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler) {handler.handle(this);}

}
