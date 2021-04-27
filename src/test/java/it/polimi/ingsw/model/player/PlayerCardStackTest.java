package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerCardStackTest {

    private Player player;
    @Before
    public void setUp() {
        player=new Player("prova");
    }

    @Test
    public void canPlaceCard() {
        Map<Resource, Integer> emptyMap1 = new HashMap<>();
        ProductionPower productionPower=new ProductionPower(emptyMap1, emptyMap1);
        DevelopmentCard developmentCard1=new DevelopmentCard(1, CardType.GREEN, emptyMap1, productionPower,0);
        DevelopmentCard developmentCard2=new DevelopmentCard(2, CardType.GREEN, emptyMap1, productionPower,0);

        assertFalse(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard2));
        assertTrue(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard1));
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard1);
        assertTrue(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard2));
        assertFalse(player.getPlayerBoard().getCardStacks().get(0).canPlaceCard(developmentCard1));
    }

    @Test
    public void testToString() {
        Map<Resource, Integer> cost = new HashMap<>();
        cost.put(Resource.SERVANT, 8);
        cost.put(Resource.STONE, 5);

        Map<Resource, Integer> output = new HashMap<>();
        output.put(Resource.SERVANT, 13);
        output.put(Resource.COIN, 3);
        output.put(Resource.STONE, 2);
        output.put(Resource.SHIELD, 10);

        Map<Resource, Integer> input = new HashMap<>();
        input.put(Resource.SERVANT, 4);
        input.put(Resource.COIN, 2);

        ProductionPower productionPower=new ProductionPower(input, output);
        DevelopmentCard developmentCard1=new DevelopmentCard(1, CardType.GREEN, cost, productionPower,4);
        DevelopmentCard developmentCard2=new DevelopmentCard(2, CardType.GREEN, cost, productionPower,4);
        DevelopmentCard developmentCard3=new DevelopmentCard(3, CardType.GREEN, cost, productionPower,4);

        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard2);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard3);
        String expected="";
        expected+=developmentCard3.toString()+"\nlevel card-1: "+developmentCard2.getLevel()
                +"\nlevel card-0: "+developmentCard1.getLevel();
        assertEquals(expected,player.getPlayerBoard().getCardStacks().get(0).toString());
    }
}