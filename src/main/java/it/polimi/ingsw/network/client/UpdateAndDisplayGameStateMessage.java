package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.ClientMessageHandler;

public class UpdateAndDisplayGameStateMessage extends ClientMessage {

    private final Game game;

    public UpdateAndDisplayGameStateMessage(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void accept(ClientMessageHandler handler) { handler.handle(this); }
}
