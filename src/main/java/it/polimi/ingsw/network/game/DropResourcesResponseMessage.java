package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.Map;

public class DropResourcesResponseMessage extends GameMessage{
    private final Map<Resource, Integer> resourcesToDrop;

    public DropResourcesResponseMessage(Map<Resource, Integer> resourcesToDrop) {
        this.resourcesToDrop = resourcesToDrop;
    }

    public Map<Resource, Integer> getResourcesToDrop() {
        return resourcesToDrop;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}

