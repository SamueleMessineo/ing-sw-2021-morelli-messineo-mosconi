package it.polimi.ingsw.network;

import it.polimi.ingsw.network.game.EndTurnMessage;
import it.polimi.ingsw.network.game.SelectCardMessage;
import it.polimi.ingsw.network.setup.CreateGameMessage;

public class SetupMessageHandler implements MessageHandler {

    public void handle(CreateGameMessage endTurnMessage) {
        System.out.println("ok");
    }
}
