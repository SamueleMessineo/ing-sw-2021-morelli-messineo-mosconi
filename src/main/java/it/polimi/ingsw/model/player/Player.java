package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains all the information about a playing during the game.
 */
public class Player implements Serializable {
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
        playedLeaderCards = new ArrayList<>();
    }

    /**
     * Returns the player's username.
     * @return The player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the total number of points accumulated by the player at any point
     * during the game.
     * @return The sum of all VPs of the player.
     */
    public int getVP() {
        int totalVP = 0;
        totalVP += playerBoard.getPoints();
        totalVP += faithTrack.getVP();
        for (LeaderCard playedLeaderCard : playedLeaderCards) {
            totalVP += playedLeaderCard.getScore();
        }

        return totalVP;
    }

    /**
     * Updates the player's score.
     * @param score increment to be added to the player's score.
     */
    public void updateScore(int score) {
        this.score += score;
    }

    @Override
    public String toString() {
        return username;
    }

    /**
     * Returns a boolean representing if the player is playing.
     * @return True if the player is currently playing, otherwise false.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Returns the player's faith track.
     * @return The FaithTrack object.
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Returns the player's personal board.
     * @return The PlayerBoard object.
     */
    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Returns the list of all the player's Leader cards.
     * @return The list of Leader cards.
     */
    public ArrayList<LeaderCard> getLeaderCards() {
        return new ArrayList<LeaderCard>(leaderCards);
    }

    /**
     * During the initial phase of the game, it allows the player to discard
     * two of his Leader cards.
     * @param selection1 index of the first leader card.
     * @param selection2 index of the second leader card.
     */
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

    /**
     * Allows to check whether a certain Leader card is playable by the player.
     * @param leaderIndex index of the Leader card.
     * @return True if the given leader card is playable, false if it's not.
     */
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

        //CardRequirements needed to play the LeaderCard
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
                if(!found)
                    return false;
            }
        }
        return true;
    }

    public void playLeaderCard(LeaderCard leaderCard){
        leaderCards.remove(leaderCard);
        playedLeaderCards.add(leaderCard);
    }

    public List<Resource> hasActiveEffectOn(String effectScope) {
        List<Resource> resources = new ArrayList<>();
        for (LeaderCard card : playedLeaderCards) {
            if (card.getEffectScope().equals(effectScope)) {
                resources.add(card.getEffectObject());
            }
        }
        return resources;
    }


    //TODO public boolean canBuyDevelopmentCard
}
