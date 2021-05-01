package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;

public class DiscardLeaderCardRequestMessage extends ClientMessage {
    private final ArrayList<LeaderCard> leaderCards;

    public DiscardLeaderCardRequestMessage(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void accept (ClientMessageHandler handler){
        handler.handle(this);
    }
}
