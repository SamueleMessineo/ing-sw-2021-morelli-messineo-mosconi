package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

public class SelectInitialResourceRequestMessage extends ClientMessage {

    private final List<Resource> resources;
    private final int amount;

    public SelectInitialResourceRequestMessage(List<Resource> resources, int amount) {
        this.resources = resources;
        this.amount = amount;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public int getAmount() {
        return amount;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
