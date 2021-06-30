package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;

/**
 * Cheat Message
 */
public class GetResourcesCheatMessage extends GameMessage {
    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }

    public void accept(LocalMessageHandler handler) {
        handler.handle(this);
    }
}
