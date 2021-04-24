package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;

public class StringMessage extends ClientMessage {
    private final String body;

    public StringMessage(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
