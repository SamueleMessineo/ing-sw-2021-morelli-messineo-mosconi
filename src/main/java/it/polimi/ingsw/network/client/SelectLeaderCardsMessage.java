package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.ClientMessageHandler;
import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.GameMessage;

import java.util.ArrayList;

public class SelectLeaderCardsMessage extends ClientMessage {
    private final ArrayList<LeaderCard> leaderCards;

    public SelectLeaderCardsMessage(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void accept (ClientMessageHandler handler){
        handler.handle(this);
    }
}
