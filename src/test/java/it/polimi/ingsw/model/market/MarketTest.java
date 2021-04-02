package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.shared.CardType;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import java.io.FileNotFoundException;

public class MarketTest {

    private Market structure;



    public MarketTest() {

        structure = new Market();

    }


    @Before
    public void setUp() throws FileNotFoundException{
        //structure.setCards();
    }
  //  @Test
//    public void getCardsGrid(){
//        for (int i = 0; i < 12; i++) {
//            System.out.println(structure.getCardsGrid().get(i).getType() +" level "+ structure.getCardsGrid().get(i).getLevel()+": ");
//            System.out.print(structure.getCardsGrid().get(i).get(0).getScore()+ " ");
//            System.out.print(structure.getCardsGrid().get(i).get(1).getScore()+ " ");
//            System.out.print(structure.getCardsGrid().get(i).get(2).getScore()+ " ");
//            System.out.println(structure.getCardsGrid().get(i).get(3).getScore());
//
//        }
   // }

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
