package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.SetupMessageHandler;

public abstract class SetupMessage implements Message {
    public void accept(SetupMessageHandler handler) {}

    public String getType() {return "SETUP";}
}
