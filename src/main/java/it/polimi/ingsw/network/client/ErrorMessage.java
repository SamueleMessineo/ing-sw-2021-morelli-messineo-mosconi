package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;

/**
 * Message with a String that would be displayed as an error.
 */
public class ErrorMessage extends ClientMessage {

    private final String body;

    public ErrorMessage(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
