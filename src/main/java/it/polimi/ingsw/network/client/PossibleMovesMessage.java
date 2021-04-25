package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

public class PossibleMovesMessage extends ClientMessage {

    private final List<String> moves;

    public PossibleMovesMessage(List<String> moves) {
        this.moves = moves;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
