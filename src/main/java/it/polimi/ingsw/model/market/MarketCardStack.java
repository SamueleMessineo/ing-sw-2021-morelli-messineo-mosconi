package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.util.Stack;

/**
 * The stack of development cards the players can buy.
 * All the cards in a stack have the same level (1, 2 or 3)
 * and type (GREEN, YELLOW, PURPLE or BLUE).
 */
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
