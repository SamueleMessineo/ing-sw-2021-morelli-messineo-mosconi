package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;

public class DropResourcesResponseMessage extends GameMessage{
    private final List<Resource> resourcesToDrop;

    public DropResourcesResponseMessage(List<Resource> resourcesToDrop) {
        this.resourcesToDrop = resourcesToDrop;
    }

    public List<Resource> getResourcesToDrop() {
        return resourcesToDrop;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }

}

