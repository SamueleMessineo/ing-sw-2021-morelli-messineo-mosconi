package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;
import java.util.Map;

public class DropResourcesResponseMessage extends GameMessage{
    private final List<Resource> resourcesToDrop;
    Map<String,List<Resource>> resourcesPlaced;

    public DropResourcesResponseMessage(Map<String,List<Resource>> resourcesPlaced, List<Resource> resourcesToDrop) {
        this.resourcesPlaced=resourcesPlaced;
        this.resourcesToDrop = resourcesToDrop;
    }

    public List<Resource> getResourcesToDrop() {
        return resourcesToDrop;
    }

    public Map<String, List<Resource>> getResourcesPlaced() {
        return resourcesPlaced;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }

}

