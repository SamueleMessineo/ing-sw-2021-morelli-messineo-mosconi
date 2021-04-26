package it.polimi.ingsw.model.shared;

import java.io.Serializable;
import java.util.Map;

/**
 * The Leader card that is given to the player ad the beginning of the game.
 */
public class LeaderCard implements Serializable {
    private static final long serialVersionUID = -3506586118929270253L;
    private final Map<Resource,Integer> resourceRequirements;
    private final Map<CardType,Integer> cardRequirements;
    private final int score;
    private final String effectScope;
    private final Resource effectObject;
    private final int cardRequirementsLevel;

    public LeaderCard(Map<Resource,Integer> resourceRequirements, Map<CardType,Integer> cardRequirements,
                      int score, String effectScope, Resource effectObject, int cardRequirementsLevel) {
    this.resourceRequirements=resourceRequirements;
    this.cardRequirements=cardRequirements;
    this.score=score;
    this.effectScope=effectScope;
    this.effectObject=effectObject;
    this.cardRequirementsLevel= cardRequirementsLevel;
    }

    /**
     * Returns the resource requirements of the card.
     * @return The resource requirements of the card.
     */
    public Map<Resource,Integer> getResourceRequirements(){
        return resourceRequirements;
    }

    /**
     * Returns the development cards required to activate this leader card.
     * @return The card requirements of the leader card.
     */
    public Map<CardType,Integer> getCardRequirements(){
        return cardRequirements;
    }

    /**
     * Returns the Vatican Points of the card.
     * @return The VPs of the card.
     */
    public int getScore(){
        return score;
    }

    /**
     * Returns the scope of the effect of the card.
     * @return The scope of the effect of the card.
     */
    public String getEffectScope(){
        return effectScope;
    }

    /**
     * Returns the object of the effect of the card.
     * @return The object of the effect of the card.
     */
    public Resource getEffectObject(){
        return effectObject;
    }

    /**
     * Returns the level of the cards required for the activation of this leader card.
     * @return The level of the required cards.
     */
    public int getCardRequirementsLevel() {
        return cardRequirementsLevel;
    }

    private String printResourceRequirements(){
        if(resourceRequirements!= null){
            return ("Resource requirements: " + resourceRequirements + "\n");
        }else {
            return "";
        }
    }

    private String printCardRequirements(){
        if(getCardRequirements()!= null){
            return ("Card requirements: " + cardRequirements + " Level: "+ cardRequirementsLevel + "\n");
        }else {
            return "";
        }
    }
    @Override
    public String toString() {
        return ("\n\n" + printResourceRequirements() + printCardRequirements() +"Score: " + score + "\n" + "Effect: " +effectScope + " " +effectObject);
    }
}
