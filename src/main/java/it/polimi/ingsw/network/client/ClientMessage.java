package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.Message;

/**
 * Message from the server to the client
 */
public abstract class ClientMessage implements Message {

    /**
     * Calls the right accept method of the ClientMessageHandler class.
     * @param clientMessageHandler the client message handler on which the accept is  to be called.
     */
    public void accept(ClientMessageHandler clientMessageHandler){}

    /**
     * The type of message.
     * @return CLIENT.
     */
    public String getType() {return "CLIENT";}
}
