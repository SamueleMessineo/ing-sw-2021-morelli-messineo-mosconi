package it.polimi.ingsw.model.market;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileNotFoundException;


/**
 * Holds the two structures that make up the game market:
 * the marble structure and the cards grid.
 */
public class Market {

    private final MarbleStructure marbleStructure;

    /**
     * Holds the 12 stacks of cards that can be bought by the players during their turn.
     */
    private List<MarketCardStack> cardsGrid = new ArrayList<>();



    public Market() {

        marbleStructure = new MarbleStructure(new ArrayList<>(), Marble.BLUE);
    }

    public void setCards() throws FileNotFoundException{
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/developmentCards.json"));

        Type listType = new TypeToken<List<DevelopmentCard>>() {}.getType();

        List<DevelopmentCard> developmentCards = gson.fromJson(reader, listType);


            for (int j = 3; j > 0; j--) {
                cardsGrid.add(new MarketCardStack(j, CardType.GREEN));

                cardsGrid.add(new MarketCardStack(j, CardType.BLUE));

                cardsGrid.add(new MarketCardStack(j, CardType.YELLOW));


                cardsGrid.add(new MarketCardStack(j, CardType.PURPLE));


            }




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
