package it.polimi.ingsw.network.client;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.network.ClientMessageHandler;

public class SelectMarblesRequestMessage extends ClientMessage {
    private final MarbleStructure marbleStructure;

    public SelectMarblesRequestMessage(MarbleStructure marbleStructure) {
        this.marbleStructure = marbleStructure;
    }

    public MarbleStructure getMarbleStructure() {
        return marbleStructure;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
