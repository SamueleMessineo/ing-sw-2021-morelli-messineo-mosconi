package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.Map;

/**
 * Message with the chosen resource to convert the white marble.
 */
public class SelectResourceForWhiteMarbleResponseMessage extends GameMessage {

    private final Map<Resource, Integer> resources;

    public SelectResourceForWhiteMarbleResponseMessage(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
    public void accept(LocalMessageHandler handler) {
        handler.handle(this);
    }
}
