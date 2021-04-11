package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.game.EndTurnMessage;
import it.polimi.ingsw.network.messages.game.SelectCardMessage;

public class MessageHandler {

    public void handle(SelectCardMessage message) {
        System.out.println(message.getNum());
    }

    public void handle(EndTurnMessage message) {
        System.out.println(message.getName());
    }
}
