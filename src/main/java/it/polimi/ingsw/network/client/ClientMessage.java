package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.Message;

public abstract class ClientMessage implements Message {

    public void accept(ClientMessageHandler clientMessageHandler){}
    public String getType() {return "CLIENT";}
}
