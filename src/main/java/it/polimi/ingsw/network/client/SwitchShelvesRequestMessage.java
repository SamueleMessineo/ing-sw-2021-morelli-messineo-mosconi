package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;

/**
 * A Message with the shelves that can be switched.
 */
public class SwitchShelvesRequestMessage extends ClientMessage{
    private final ArrayList<String> shelves;

    public SwitchShelvesRequestMessage(ArrayList<String> shelves) {
        this.shelves = shelves;
    }

    public ArrayList<String> getShelves() {
        return shelves;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
