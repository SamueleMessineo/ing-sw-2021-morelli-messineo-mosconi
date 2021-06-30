package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;

/**
 * Message with the players not yet played leader cards.
 */
public class DropLeaderCardRequestMessage extends ClientMessage {
    private final ArrayList<LeaderCard> leaderCards;

    public DropLeaderCardRequestMessage(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void accept (ClientMessageHandler handler){
        handler.handle(this);
    }
}
