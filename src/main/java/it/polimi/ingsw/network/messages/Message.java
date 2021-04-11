package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.MessageHandler;

import java.io.Serializable;

public interface Message extends Serializable {
    void accept(MessageHandler handler);
}
