package it.polimi.ingsw.model.market;

import java.util.ArrayList;

/**
 * Holds the two structures that make up the game market:
 * the marble structure and the cards grid.
 */
public class Market {
    private final CardsMarket cardsMarket;
    private final MarbleStructure marbleStructure;

    public Market() {
        cardsMarket = new CardsMarket();
        marbleStructure = new MarbleStructure(new ArrayList<>(), Marble.BLUE);
    }

    public CardsMarket getCardsMarket() {
        return cardsMarket;
    }

    public MarbleStructure getMarbleStructure() {
        return marbleStructure;
    }
}
