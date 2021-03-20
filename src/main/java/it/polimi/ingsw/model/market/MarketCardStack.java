package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.util.Stack;

public class MarketCardStack extends Stack<DevelopmentCard> {
    private final int level;
    private final CardType type;

    public MarketCardStack(int level, CardType type) {
        super();
        this.level = level;
        this.type = type;
    }

    public CardType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }
}
