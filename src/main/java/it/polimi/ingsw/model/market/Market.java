package it.polimi.ingsw.model.market;

/**
 * Holds the two structures that make up the game market:
 * the marble structure and the cards grid.
 */
public class Market {
    private final CardsMarket cardsMarket = new CardsMarket();
    private final MarbleStructure marbleStructure = new MarbleStructure();

    public CardsMarket getCardsMarket() {
        return cardsMarket;
    }

    public MarbleStructure getMarbleStructure() {
        return marbleStructure;
    }
}
