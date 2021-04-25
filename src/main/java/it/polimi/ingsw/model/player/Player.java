package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Player {
    private final String username;
    private int score = 0;
    private boolean playing = false;
    private final FaithTrack faithTrack;
    private final PlayerBoard playerBoard;
    private ArrayList<LeaderCard> leaderCards;
    private ArrayList<LeaderCard> playedLeaderCards;

    public Player(String username) {
        this.username = username;
        faithTrack = new FaithTrack();
        playerBoard = new PlayerBoard();
    }

    public String getUsername() {
        return username;
    }

    public int getVP() {
        int totalVP = 0;
        totalVP += playerBoard.getPoints();
        totalVP += faithTrack.getVP();
        for (LeaderCard playedLeaderCard : playedLeaderCards) {
            totalVP += playedLeaderCard.getScore();
        }

        return totalVP;
    }



    public void updateScore(int score) {
        this.score += score;
    }

    @Override
    public String toString() {
        return username;
    }

    public boolean isPlaying() {
        return playing;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return new ArrayList<LeaderCard>(leaderCards);
    }

    public void dropInitialLeaderCards(int selection1, int selection2){
        LeaderCard card1 = leaderCards.get(selection1);
        LeaderCard card2 = leaderCards.get(selection2);
        leaderCards.remove(card1);
        leaderCards.remove(card2);
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ArrayList<LeaderCard> getPlayedLeaderCards() {
        return playedLeaderCards;
    }

    public boolean canPlayLeader(int leaderIndex) {
        LeaderCard leader = leaderCards.get(leaderIndex);
        Map<CardType,Integer> cardRequirements=leader.getCardRequirements();
        int cardRequirementsLevel= leader.getCardRequirementsLevel();

        //Resource for play the LeaderCard
        Map<Resource, Integer> resourceRequirements = leader.getResourceRequirements();
        if(!resourceRequirements.isEmpty() && resourceRequirements!=null){
            Map<Resource, Integer> allResources = playerBoard.getResources();
            for (Resource resource : allResources.keySet()) {
                if (allResources.get(resource) < resourceRequirements.get(resource)) return false;
            }
        }

        //CardRequirements for play the LeaderCard
        if(cardRequirements!=null && !cardRequirements.isEmpty())
        {
            for(CardType cardType: cardRequirements.keySet()){
                int i = cardRequirements.get(cardType);
                boolean found = false;
                for(PlayerCardStack playerCardStack: playerBoard.getCardStacks()){
                    for(DevelopmentCard playerCard: playerCardStack){
                        if(playerCard.getCardType()==cardType &&
                                (playerCard.getLevel()==cardRequirementsLevel || cardRequirementsLevel==0)) {
                            i--;
                        }
                    }
                }
                if(i==0)
                    found = true;
                if(found == false)
                    return false;
            }
        }
        return true;
    }

    //TODO public boolean canBuyDevelopmentCard
}
