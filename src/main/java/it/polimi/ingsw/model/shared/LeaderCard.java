package it.polimi.ingsw.model.shared;

import java.util.HashMap;
import java.util.Map;

public class LeaderCard {
    private Map<Resource,Integer> resourceRequirements;
    private Map<CardType,Integer> cardRequirements;
    private int score;
    private String effectScope;
    private Resource effectObject;

    public LeaderCard(Map<Resource,Integer> resourceRequirements, Map<CardType,Integer> cardRequirements,
                      int score, String effectScope, Resource effectObject) {
    this.resourceRequirements=resourceRequirements;
    this.cardRequirements=cardRequirements;
    this.score=score;
    this.effectScope=effectScope;
    this.effectObject=effectObject;
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
}
