package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;

public class SelectResourceForWhiteMarbleResponseMessage extends GameMessage {

    private final List<Resource> resources;

    public SelectResourceForWhiteMarbleResponseMessage(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler) {
        handler.handle(this);
    }
}
