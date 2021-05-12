package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

public class SelectStackToPlaceCardRequestMessage extends ClientMessage {
    private final List<Integer> stacks;

    public SelectStackToPlaceCardRequestMessage(List<Integer> stacks) {
        this.stacks = stacks;
    }

    public List<Integer> getStacks() {
        return stacks;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }

}
