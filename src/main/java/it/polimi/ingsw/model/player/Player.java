package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.utils.GameUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    private boolean active = true;

    /**
     * Player class constructor.
     */
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

    /**
     * A player can discard a leader card from his hand to gain a faith point
     * @param card index of the card to discard
     */
    public void dropLeaderCard(int card){
        LeaderCard sel = leaderCards.get(card);
        leaderCards.remove(sel);
    }

    /**
     * Sets player leader cards.
     * @param leaderCards a list of 4 leader cards.
     */
    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * Player leader cards.
     * @return a List with player leader cards.
     */
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

        Map<Resource, Integer> resourceRequirements = leader.getResourceRequirements();


        if(resourceRequirements != null){
            Map<Resource, Integer> fullResourceRequirements = new HashMap<>(){{
                put(Resource.STONE, 0);
                put(Resource.COIN, 0);
                put(Resource.SHIELD,0);
                put(Resource.SERVANT, 0);
                putAll(resourceRequirements);
            }};
            Map<Resource, Integer> allResources = playerBoard.getResources();
            for (Resource resource : allResources.keySet()) {
                if (allResources.get(resource) < fullResourceRequirements.get(resource)) return false;
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
                if(i<1)
                    found = true;
                if(!found)
                    return false;
            }
        }
        return true;
    }

    /**
     * Removes a leader card form leaderCards list and adds it to played leader class.
     * @param leaderCard the LeaderCard that the player is playing.
     */
    public void playLeaderCard(LeaderCard leaderCard){
        leaderCards.remove(leaderCard);
        playedLeaderCards.add(leaderCard);
    }

    /**
     * Return the EffectObjects, if present, associated with an EffectScope.
     * @param effectScope a String of which it is interested to know if the Player has active effects of that type.
     * @return the Objects of activeLeaders of the Player that have effectScope as scope.
     */
    public List<Resource> hasActiveEffectOn(String effectScope) {
        List<Resource> resources = new ArrayList<>();
        for (LeaderCard card : playedLeaderCards) {
            if (card.getEffectScope().equals(effectScope)) {
                resources.add(card.getEffectObject());
            }
        }
        return resources;
    }

    /**
     * Checks if the player has the resources to buy and place a development card.
     * @param developmentCard the development card of interest.
     * @return true if the player can buy and place the development card.
     */
    public boolean canBuyAndPlaceDevelopmentCard(DevelopmentCard developmentCard){
        Map<Resource, Integer> cardCost = computeDiscountedCost(developmentCard);

        return playerBoard.canPayResources(cardCost)
                && playerBoard.canPlaceDevelopmentCard(developmentCard);
    }

    /**
     * Checks the price of a development card for the player.
     * @param developmentCard the Development card of interest.
     * @return a Map<Resource, Integer> with the cost of the card.
     */
    public Map<Resource, Integer> computeDiscountedCost(DevelopmentCard developmentCard){
        Map<Resource, Integer> cost = developmentCard.getCost();

        for (LeaderCard leaderCard:
                playedLeaderCards) {
            if(leaderCard.getEffectScope().equals("Market")){
                cost.put(leaderCard.getEffectObject(), cost.get(leaderCard.getEffectObject())-1);
            }
        }
        return  cost;
    }

    /**
     * Checks if the player can activate productions.
     * @return true if the player can activate at least one production.
     */
    public boolean canActivateProduction() {
        Map<Resource, Integer> allResources = new HashMap<>(playerBoard.getResources());
        allResources = GameUtils.sumResourcesMaps(allResources, GameUtils.emptyResourceMap());
        for (PlayerCardStack cardStack : playerBoard.getCardStacks()) {
            if (!cardStack.empty()) {
                DevelopmentCard topCard = cardStack.peek();
                boolean canActivateCard = true;
                for (Resource resource : topCard.getProductionPower().getInput().keySet()) {
                    if (resource == Resource.FAITH)continue;
                    if (allResources.get(resource) < topCard.getProductionPower().getInput().get(resource)) {
                        canActivateCard = false;
                        break;
                    }
                }
                if (canActivateCard) {
                    return true;
                }
            }
        }

        if(!getPlayerBoard().getExtraProductionPowers().isEmpty()){
            for (ProductionPower productionPower:
                    getPlayerBoard().getExtraProductionPowers()) {
                boolean canActivatePower = true;
                for (Resource resource : productionPower.getInput().keySet()) {
                    if (allResources.get(resource) < productionPower.getInput().get(resource)) {
                        canActivatePower = false;
                        break;
                    }
                }
                if (canActivatePower) {
                    return true;
                }
            }
            }

        return false;
    }

    /**
     * Checks which  development  cards have a production power that can be activated.
     * @return a List with the development cards the player can activate the production on.
     */
    public ArrayList<DevelopmentCard> possibleDevelopmentCardProduction(){
        Map<Resource, Integer> allResources = new HashMap<>(playerBoard.getResources());
        ArrayList<DevelopmentCard> developmentCardsToActive=new ArrayList<>();
        for (PlayerCardStack cardStack : playerBoard.getCardStacks()) {
            if (!cardStack.empty()) {
                DevelopmentCard topCard = cardStack.peek();
                developmentCardsToActive.add(topCard);
                for (Resource resource : topCard.getProductionPower().getInput().keySet()) {
                    if (allResources.get(resource) < topCard.getProductionPower().getInput().get(resource)) {
                        developmentCardsToActive.remove(topCard);
                        break;
                    }
                }
            }
        }
        return developmentCardsToActive;
    }

    /**
     * Return player's extra production that can be activated.
     * @return a List with the extra productions that the player can activate.
     */
    public ArrayList<ProductionPower> possibleExtraProductionPower(){
        Map<Resource, Integer> allResources = new HashMap<>(playerBoard.getResources());
        ArrayList<ProductionPower> extraProductionPowersToActive=new ArrayList<>();
        if(getPlayerBoard().getExtraProductionPowers() != null){
            for (ProductionPower productionPower:
                    getPlayerBoard().getExtraProductionPowers()) {
                for (Resource resource : productionPower.getInput().keySet()) {
                    if (productionPower.getInput().get(resource)!= null && allResources.get(resource) >= productionPower.getInput().get(resource)) {
                        extraProductionPowersToActive.add(productionPower);
                    }
                }
            }
        }
        return extraProductionPowersToActive;
    }

    /**
     * Return player's production that can be activated.
     * @return a List with the productions that the player can activate.
     */
    public ArrayList<ProductionPower> possibleProductionPowersToActive(){
        ArrayList<ProductionPower> productionPowers=new ArrayList<>();
        if(canActivateProduction()) {
            for (DevelopmentCard developmentCard : possibleDevelopmentCardProduction()) {
                productionPowers.add(developmentCard.getProductionPower());
            }
            productionPowers.addAll(possibleExtraProductionPower());
            return productionPowers;
        }
        return productionPowers;
    }

    /**
     * Checks if the player can activate a basic production.
     * @return true if the player has at least 2 resources.
     */
    public boolean canActivateBasicProduction(){
        Map<Resource,Integer> resources=new HashMap<>(playerBoard.getResources());
        int res=0;

        for(Resource resource : resources.keySet()){
            res+=resources.get(resource);
            if(res>=2)
                return true;
        }
        return false;
    }

    /**
     * Checks if the player is active.
     * @return true if the player is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sests player active/inactive status.
     * @param active true if the player is active.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
