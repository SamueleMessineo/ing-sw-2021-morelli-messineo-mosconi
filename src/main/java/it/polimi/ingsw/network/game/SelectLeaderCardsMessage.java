package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.GameMessageHandler;
import it.polimi.ingsw.network.Message;

public class SelectLeaderCardsMessage extends GameMessage {
    private LeaderCard leaderCard1;
    private LeaderCard getLeaderCard2;

    public SelectLeaderCardsMessage(LeaderCard leaderCard1, LeaderCard getLeaderCard2) {
        this.leaderCard1 = leaderCard1;
        this.getLeaderCard2 = getLeaderCard2;
    }

    public LeaderCard getLeaderCard1() {
        return leaderCard1;
    }

    public void setLeaderCard1(LeaderCard leaderCard1) {
        this.leaderCard1 = leaderCard1;
    }

    public LeaderCard getGetLeaderCard2() {
        return getLeaderCard2;
    }

    public void setGetLeaderCard2(LeaderCard getLeaderCard2) {
        this.getLeaderCard2 = getLeaderCard2;
    }

    public void accept (GameMessageHandler handler){
        handler.handle(this);
    }
}
