package it.polimi.ingsw.network;

import it.polimi.ingsw.network.game.EndTurnMessage;
import it.polimi.ingsw.network.game.SelectCardMessage;

public class GameMessageHandler {

    public void handle(SelectCardMessage message) {
        System.out.println(message.getNum());
    }

    public void handle(EndTurnMessage message) {
        System.out.println(message.getName());
    }


}
