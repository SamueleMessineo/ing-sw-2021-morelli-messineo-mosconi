package it.polimi.ingsw.model.market;

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
