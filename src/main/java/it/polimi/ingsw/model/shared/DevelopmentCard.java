package it.polimi.ingsw.model.shared;


import java.util.Map;

public class DevelopmentCard {
    private final int level;
    private final CardType cardType;
    private final Map<Resource,Integer> cost;
    private final ProductionPower  productionPower;
    private final int score;

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

    public int getLevel() {
        return level;
    }

    public CardType getCardType() {
        return cardType;
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return cardType + "\n" + level + "\n" + cost + "\n" + productionPower + "\n" + score;
    }
}
