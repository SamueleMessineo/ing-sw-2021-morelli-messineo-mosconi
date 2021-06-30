package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;

/**
 * Message with the chosen initial resources.
 */
public class SelectInitialResourceResponseMessage extends GameMessage {

    private final List<Resource> selectedResources;

    public SelectInitialResourceResponseMessage(List<Resource> selectedResources) {
        this.selectedResources = selectedResources;
    }

    public List<Resource> getSelectedResources() {
        return selectedResources;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
