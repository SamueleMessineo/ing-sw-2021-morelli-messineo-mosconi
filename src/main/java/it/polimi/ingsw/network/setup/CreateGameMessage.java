package it.polimi.ingsw.network.setup;

import it.polimi.ingsw.network.SetupMessageHandler;

public class CreateGameMessage extends SetupMessage {

    public String getUsername() {
        String username = "samupuzza";
        return username;
    }

    public void accept(SetupMessageHandler handler) {
        handler.handle(this);
    }
}
