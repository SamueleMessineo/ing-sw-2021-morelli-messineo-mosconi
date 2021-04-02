package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public int getScore() {
        return score;
    }

    public void updateScore(int score) {
        this.score += score;
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
        return leaderCards;
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
        Map<Resource, Integer> allResources = playerBoard.getResources();
        for (Resource resource : allResources.keySet()) {
            if (allResources.get(resource) < resourceRequirements.get(resource)) return false;
        }

        //CardRequirements for play the LeaderCard
        for(CardType cardType: cardRequirements.keySet()){
            boolean found=false;
           for(PlayerCardStack playerCardStack: playerBoard.getCardStacks()){
               for(DevelopmentCard playerCard: playerCardStack){
                   if(playerCard.getCardType()==cardType &&
                           (playerCard.getLevel()==cardRequirementsLevel || cardRequirementsLevel==0))
                       found=true;
               }
           }
           if(found == false)
               return false;
        }
        return true;
    }
}
