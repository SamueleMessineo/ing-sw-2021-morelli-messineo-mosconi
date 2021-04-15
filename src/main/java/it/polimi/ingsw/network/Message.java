package it.polimi.ingsw.network;

import java.io.Serializable;

public interface Message extends Serializable {
    String getType();
}
