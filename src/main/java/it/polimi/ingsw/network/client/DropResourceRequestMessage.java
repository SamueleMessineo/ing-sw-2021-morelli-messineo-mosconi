package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;
import java.util.Map;

public class DropResourceRequestMessage extends ClientMessage {
    private final Map<Resource, Integer> resources;

    public DropResourceRequestMessage(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }

}
