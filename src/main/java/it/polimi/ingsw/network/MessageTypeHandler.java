package it.polimi.ingsw.network;

import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.network.setup.SetupMessage;

public class MessageTypeHandler implements MessageHandler{

    private final GameMessageHandler gameMessageHandler = new GameMessageHandler();
    private final SetupMessageHandler setupMessageHandler = new SetupMessageHandler();

    public void handle(GameMessage message) {
        message.accept(gameMessageHandler);
    }

    public void handle(SetupMessage message) {
        message.accept(setupMessageHandler);
    }
}
