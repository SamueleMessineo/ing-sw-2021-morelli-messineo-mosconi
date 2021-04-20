package it.polimi.ingsw.network;

import it.polimi.ingsw.network.game.EndTurnMessage;
import it.polimi.ingsw.network.game.SelectCardMessage;
import it.polimi.ingsw.network.game.SelectLeaderCardsMessage;
import it.polimi.ingsw.network.game.SelectMarblesMessage;

public class GameMessageHandler {

    public void handle(SelectCardMessage message) {
        System.out.println(message.getNum());
    }

    public void handle(EndTurnMessage message) {
        System.out.println(message.getName());
    }

    public void handle(SelectLeaderCardsMessage message) {
//        For DEBUG
        System.out.println("SelectLeaderCardMessage");
//
    }

    public void handle(SelectMarblesMessage message) {
//        For DEBUG
        System.out.println("SelectMarblesMessage");
//
    }

}
