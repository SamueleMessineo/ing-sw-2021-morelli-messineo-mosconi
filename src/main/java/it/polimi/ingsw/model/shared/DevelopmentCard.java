package it.polimi.ingsw.model.shared;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The development card that the player can buy from the market.
 */
public class DevelopmentCard implements Serializable {
    private static final long serialVersionUID = -3506586118929270253L;
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

    /**
     * Returns the cost of the development card.
     * @return The cost of the development card.
     */
    public Map<Resource,Integer> getCost(){
        Map<Resource, Integer> costMap = new HashMap<>() {{
           put(Resource.COIN, 0);
           put(Resource.SERVANT, 0);
           put(Resource.SHIELD, 0);
           put(Resource.STONE, 0);
        }};
        costMap.putAll(cost);
        return costMap;
    }

    /**
     * Returns the level of the development card.
     * @return The level of the development card.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the type of the development card.
     * @return The type of the development card.
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Returns the production power of the development card.
     * @return The production power of the development card.
     */
    public ProductionPower getProductionPower() {
        return productionPower;
    }

    /**
     * Returns the Vatican Points of the card.
     * @return The VPs of the card.
     */
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return cardType + "\nlevel: " + level + "\n" + cost + "\n" + productionPower.toString() + "\nscore: " + score;
    }
}
