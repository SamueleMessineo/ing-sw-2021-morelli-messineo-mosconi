package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.ClientMessageHandler;

public class SelectResourceForWhiteMarbleRequestMessage extends ClientMessage {

    private final int numberOfWhiteMarbles;
    private final Resource firstResource;
    private final Resource secondResource;

    public SelectResourceForWhiteMarbleRequestMessage(int numberOfWhiteMarbles, Resource firstResource, Resource secondResource) {
        this.numberOfWhiteMarbles = numberOfWhiteMarbles;
        this.firstResource = firstResource;
        this.secondResource = secondResource;
    }

    public int getNumberOfWhiteMarbles() {
        return numberOfWhiteMarbles;
    }

    public Resource getFirstResource() {
        return firstResource;
    }

    public Resource getSecondResource() {
        return secondResource;
    }

    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
