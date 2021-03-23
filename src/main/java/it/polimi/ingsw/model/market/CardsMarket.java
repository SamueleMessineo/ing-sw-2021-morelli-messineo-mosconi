package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.shared.CardType;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the 12 stacks of cards that can be bought by the players during their turn.
 */
public class CardsMarket {
    private final List<MarketCardStack> cardsGrid = new ArrayList<>();

    public List<MarketCardStack> getCardsGrid() {
        return cardsGrid;
    }

    /**
     * Get a stack of cards based on it's level and type.
     * @param level the level of the stack to retrieve.
     * @param type the type of the stack to retrieve.
     * @return The MarketCardStack with the given level and type.
     */
    public MarketCardStack getStackByLevelAndType(int level, CardType type) {
        int col;
        switch (type) {
            case GREEN: col = 0;
                break;
            case BLUE: col = 1;
                break;
            case YELLOW: col = 2;
                break;
            case PURPLE: col = 3;
                break;
            default: col = 100000;
        }
        
        return cardsGrid.get((3 - level) * 4 + col);
    }
}