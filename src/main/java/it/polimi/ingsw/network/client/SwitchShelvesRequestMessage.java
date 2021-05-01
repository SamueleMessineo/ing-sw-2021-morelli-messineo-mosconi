package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;

public class SwitchShelvesRequestMessage extends ClientMessage{
    private final ArrayList<Shelf> shelves;

    public SwitchShelvesRequestMessage(ArrayList<Shelf> shelves) {
        this.shelves = shelves;
    }

    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
