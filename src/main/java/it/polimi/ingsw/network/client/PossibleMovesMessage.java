package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.ClientMessageHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PossibleMovesMessage extends ClientMessage  {

    private final ArrayList<String> moves;

    public PossibleMovesMessage(ArrayList<String> moves) {
        this.moves = new ArrayList<>(moves);
    }

    public List<String> getMoves() {
        return moves;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }

}
