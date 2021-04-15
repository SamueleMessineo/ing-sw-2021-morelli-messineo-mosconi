package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.MessageHandler;
import it.polimi.ingsw.network.SetupMessageHandler;

public class CreateGameMessage extends SetupMessage {

    public void accept(SetupMessageHandler handler) {
        handler.handle(this);
    }
}
