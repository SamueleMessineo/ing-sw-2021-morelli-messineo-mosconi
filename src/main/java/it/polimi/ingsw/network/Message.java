package it.polimi.ingsw.network;

import java.io.Serializable;

/**
 * Interface Message implemented by the various types of messages.
 */
public interface Message extends Serializable {
    String getType();
}
