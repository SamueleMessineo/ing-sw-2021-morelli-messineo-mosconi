package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.shared.CardType;

import java.util.ArrayList;
import java.util.List;

public class CardsMarket {
    private final List<MarketCardStack> cardsGrid = new ArrayList<>();

    public List<MarketCardStack> getCardsGrid() {
        return cardsGrid;
    }

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