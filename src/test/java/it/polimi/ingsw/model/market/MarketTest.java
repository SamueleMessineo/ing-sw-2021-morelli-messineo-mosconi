package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.CardType;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import java.io.FileNotFoundException;

public class MarketTest {

    private Game game;
    private Market structure;


    @Before
    public void setUp() throws FileNotFoundException{
        game = new Game();
        structure = game.getMarket();
    }


    @Test
    public void getCardsGridTest(){
        assertEquals(12, structure.getCardsGrid().stream().count());
        for (int i = 0; i < 12; i++) {
           assertEquals(4, structure.getCardsGrid().get(i).stream().distinct().count());
            for (int j = 0; j < 3; j++) {
                assertEquals(structure.getCardsGrid().get(i).get(j).getLevel(), structure.getCardsGrid().get(i).get(j+1).getLevel());
                assertEquals(structure.getCardsGrid().get(i).get(j).getCardType(), structure.getCardsGrid().get(i).get(j+1).getCardType());
            }
        }
    }

    @Test
    public void getStackByLevelAndTypeTest(){
        assertEquals(structure.getCardsGrid().get(0), structure.getStackByLevelAndType(3, CardType.GREEN));
        assertEquals(structure.getCardsGrid().get(1), structure.getStackByLevelAndType(3, CardType.BLUE));
        assertEquals(structure.getCardsGrid().get(2), structure.getStackByLevelAndType(3, CardType.YELLOW));
        assertEquals(structure.getCardsGrid().get(3), structure.getStackByLevelAndType(3, CardType.PURPLE));
        assertEquals(structure.getCardsGrid().get(4), structure.getStackByLevelAndType(2, CardType.GREEN));
        assertEquals(structure.getCardsGrid().get(5), structure.getStackByLevelAndType(2, CardType.BLUE));
        assertEquals(structure.getCardsGrid().get(6), structure.getStackByLevelAndType(2, CardType.YELLOW));
        assertEquals(structure.getCardsGrid().get(7), structure.getStackByLevelAndType(2, CardType.PURPLE));
        assertEquals(structure.getCardsGrid().get(8), structure.getStackByLevelAndType(1, CardType.GREEN));
        assertEquals(structure.getCardsGrid().get(9), structure.getStackByLevelAndType(1, CardType.BLUE));
        assertEquals(structure.getCardsGrid().get(10), structure.getStackByLevelAndType(1, CardType.YELLOW));
        assertEquals(structure.getCardsGrid().get(11), structure.getStackByLevelAndType(1, CardType.PURPLE));
    }
}
