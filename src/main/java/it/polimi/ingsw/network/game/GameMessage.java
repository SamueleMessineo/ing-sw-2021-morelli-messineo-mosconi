package it.polimi.ingsw.network.game;

import it.polimi.ingsw.client.LocalMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;

/**
 * Message from the client to the server regarding the game.
 */
public abstract class GameMessage implements Message {

    /**
     * Calls the right accept method of the GameMessageHandler class.
     * @param handler the game message handler on which the accept is  to be called.
     */
    public void accept(GameMessageHandler handler) {}
    /**
     * Calls the right accept method of the LocalMessageHandler class.
     * @param handler the local message handler on which the accept is  to be called.
     */
    public void accept(LocalMessageHandler handler) {}

    /**
     * The type of message.
     * @return GAME.
     */
    public String getType() {return "GAME";}
}
