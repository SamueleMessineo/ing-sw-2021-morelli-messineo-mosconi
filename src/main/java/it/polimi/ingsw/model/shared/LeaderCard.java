package it.polimi.ingsw.model.shared;

import java.util.HashMap;
import java.util.Map;

public class LeaderCard {
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

    public Map<Resource,Integer> getResourceRequirements(){
        return resourceRequirements;
    }

    public Map<CardType,Integer> getCardRequirements(){
        return cardRequirements;
    }

    public int getScore(){
        return score;
    }

    public String getEffectScope(){
        return effectScope;
    }

    public Resource getEffectObject(){
        return effectObject;
    }

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
