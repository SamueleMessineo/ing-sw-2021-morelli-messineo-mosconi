package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

public class DropResourceRequestMessage extends ClientMessage {
    private final List<Resource> resources;

    public DropResourceRequestMessage(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }

}
