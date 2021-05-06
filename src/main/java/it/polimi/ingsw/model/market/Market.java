package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Holds the two structures that make up the game market:
 * the marble structure and the cards grid.
 */
public class Market implements Serializable {

    private final MarbleStructure marbleStructure;

    /**
     * The 12 stacks of cards that can be bought by the players during their turn.
     */
    private final List<MarketCardStack> cardsGrid = new ArrayList<>();

    public Market() {
        List<Marble> initialMarbles = new ArrayList<>();
        initialMarbles.add(Marble.RED);
        for (int i = 0; i < 2; i++) {
            initialMarbles.add(Marble.GREY);
            initialMarbles.add(Marble.BLUE);
            initialMarbles.add(Marble.YELLOW);
            initialMarbles.add(Marble.PURPLE);
        }
        for (int i = 0; i < 4; i++) {
            initialMarbles.add(Marble.WHITE);
        }

        Collections.shuffle(initialMarbles);
        marbleStructure = new MarbleStructure(new ArrayList<>(initialMarbles.subList(0, initialMarbles.size()-1)), initialMarbles.get(initialMarbles.size()-1));

        for (int j = 3; j > 0; j--) {
            cardsGrid.add(new MarketCardStack(j, CardType.GREEN));
            cardsGrid.add(new MarketCardStack(j, CardType.BLUE));
            cardsGrid.add(new MarketCardStack(j, CardType.YELLOW));
            cardsGrid.add(new MarketCardStack(j, CardType.PURPLE));
        }

    }

    /**
     * Places the development cards in their respective stacks inside the cardsGrid.
     * @param developmentCards list of all development cards.
     */
    public void setCards(ArrayList<DevelopmentCard> developmentCards){
        int i=0;
        int j = 4;
        for (int k = 0; k < 12; k++) {

            MarketCardStack cardStack = cardsGrid.get(k);
            cardStack.addAll(developmentCards.subList(i, j));

            Collections.shuffle(cardStack);

            j+=4;
            i+=4;
        }
    }

    public MarbleStructure getMarbleStructure() {
        return marbleStructure;
    }

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
