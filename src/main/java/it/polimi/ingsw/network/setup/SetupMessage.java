package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SetupMessageHandler;

public abstract class SetupMessage extends Message {
    void accept(SetupMessageHandler handler) {}
}
