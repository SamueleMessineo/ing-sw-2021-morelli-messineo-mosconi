package it.polimi.ingsw.network.pingpong;

import it.polimi.ingsw.network.Message;

public abstract class ConnectionMessage implements Message {
    /**
     * The type of message.
     * @return CONNECTION.
     */
    @Override
    public String getType() {
        return "CONNECTION";
    }
}
