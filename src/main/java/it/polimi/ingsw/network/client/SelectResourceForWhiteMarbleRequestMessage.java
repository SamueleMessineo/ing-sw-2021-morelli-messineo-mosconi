package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

/**
 * Message with options of conversion for the white marbles.
 */
public class SelectResourceForWhiteMarbleRequestMessage extends ClientMessage {

    private final int numberOfWhiteMarbles;
    private final List<Resource> options;

    public SelectResourceForWhiteMarbleRequestMessage(int numberOfWhiteMarbles, List<Resource> options) {
        this.numberOfWhiteMarbles = numberOfWhiteMarbles;
        this.options = options;
    }

    public int getNumberOfWhiteMarbles() {
        return numberOfWhiteMarbles;
    }

    public List<Resource> getOptions() {
        return options;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
