package it.polimi.ingsw.network;

import it.polimi.ingsw.network.setup.CreateGameMessage;

public class SetupMessageHandler {

    public void handle(CreateGameMessage createGameMessage) {
        System.out.println(createGameMessage.getUsername());
    }
}
