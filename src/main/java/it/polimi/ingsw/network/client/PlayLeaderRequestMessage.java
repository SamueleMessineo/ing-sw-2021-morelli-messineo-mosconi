package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Message with the leader cards the player can play.
 */
public class PlayLeaderRequestMessage extends ClientMessage{
    private final List<LeaderCard> leaderCards;

    public PlayLeaderRequestMessage(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void accept (ClientMessageHandler handler){
        handler.handle(this);
    }
}
