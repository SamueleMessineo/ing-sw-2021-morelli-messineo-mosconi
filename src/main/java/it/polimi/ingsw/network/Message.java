package it.polimi.ingsw.network;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public void accept(MessageHandler handler) {};
}
