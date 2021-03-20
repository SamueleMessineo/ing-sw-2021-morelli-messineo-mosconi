package it.polimi.ingsw.model.shared;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCard {
    private int level;
    private CardType cardType;
    private Map<Resource,Integer> cost;
    private ProductionPower  productionPower;
    private int score;

    public DevelopmentCard(int level, CardType cardType, Map<Resource,Integer> cost,
                           ProductionPower productionPower, int score){
        this.level=level;
        this.cardType=cardType;
        this.cost=cost;
        this.productionPower=productionPower;
        this.score=score;
    }

    public Map<Resource,Integer> getCost(){
        return cost;
    }
}
