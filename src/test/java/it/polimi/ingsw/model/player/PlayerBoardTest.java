package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerBoardTest {
    private PlayerBoard playerBoard;
    @Before
    public void setUp() throws Exception {
        playerBoard = new PlayerBoard();
        Map<Resource, Integer> add_res=new HashMap<>();
        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);

        playerBoard.getStrongbox().addResources(add_res);

        Shelf bottomShelf = playerBoard.getWarehouse().getShelf("bottom");
        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,3);
        bottomShelf.addResources(shelfRes);

        playerBoard.getWarehouse().addNewShelf("extra1", new Shelf(2, Resource.COIN));

        Shelf extraShelf = playerBoard.getWarehouse().getShelf("extra1");

        Map<Resource,Integer> extraShelfRes =new HashMap<>();
        extraShelfRes.put(Resource.COIN,1);
        extraShelf.addResources(extraShelfRes);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void activateProduction() {
        Map<Resource,Integer> emptyMap=new HashMap<>();
        Map<Resource,Integer> input=new HashMap<>();
        Map<Resource,Integer> output=new HashMap<>();

        input.put(Resource.SERVANT, 5);
        input.put(Resource.COIN, 5);

        output.put(Resource.STONE, 5);
        output.put(Resource.SHIELD, 5);

        ProductionPower productionPower=new ProductionPower(input,output);
        DevelopmentCard developmentCard=new DevelopmentCard(1, CardType.PURPLE,emptyMap,productionPower,0);
        playerBoard.getCardStacks().get(0).push(developmentCard);

        Map<Resource, Integer> strongboxResources=new HashMap<>();
        strongboxResources.put(Resource.SERVANT, 7);
        strongboxResources.put(Resource.COIN, 5);
        strongboxResources.put(Resource.STONE, 14);
        strongboxResources.put(Resource.SHIELD, 20);

        Map<Resource, Integer> bottomResources=new HashMap<>();
        bottomResources.put(Resource.SERVANT, 0);
        bottomResources.put(Resource.COIN, 0);
        bottomResources.put(Resource.STONE, 0);
        bottomResources.put(Resource.SHIELD, 0);

        List<Integer> selectedStacks=new ArrayList<>();
        selectedStacks.add(0);
        playerBoard.activateProduction(selectedStacks);
        assertEquals(strongboxResources,playerBoard.getStrongbox().getResources());
        assertEquals(bottomResources, playerBoard.getWarehouse().getShelf("bottom").getResources());
        assertEquals(bottomResources, playerBoard.getWarehouse().getShelf("extra1").getResources());

    }

    @Test
    public void canPlaceDevelopmentCard() {
        Map<Resource,Integer> emptyMap=new HashMap<>();
        ProductionPower productionPower=new ProductionPower(emptyMap,emptyMap);

        DevelopmentCard developmentCard=new DevelopmentCard(1, CardType.PURPLE,emptyMap,productionPower,0);
        assertTrue(playerBoard.canPlaceDevelopmentCard(developmentCard));
        playerBoard.getCardStacks().get(0).push(developmentCard);

        developmentCard=new DevelopmentCard(3, CardType.PURPLE,emptyMap,productionPower,0);
        assertFalse(playerBoard.canPlaceDevelopmentCard(developmentCard));

        developmentCard=new DevelopmentCard(2, CardType.PURPLE,emptyMap,productionPower,0);
        assertTrue(playerBoard.canPlaceDevelopmentCard(developmentCard));
        playerBoard.getCardStacks().get(0).push(developmentCard);

        developmentCard=new DevelopmentCard(3, CardType.PURPLE,emptyMap,productionPower,0);
        assertTrue(playerBoard.canPlaceDevelopmentCard(developmentCard));
    }

    @Test
    public void canPayResources() {
        Map<Resource,Integer> cost=new HashMap<>();
        cost.put(Resource.COIN, 8);
        cost.put(Resource.STONE, 8);
        cost.put(Resource.SHIELD, 16);
        assertFalse(playerBoard.canPayResources(cost));

        cost.put(Resource.COIN, 8);
        cost.put(Resource.STONE, 8);
        cost.put(Resource.SHIELD, 1);
        assertTrue(playerBoard.canPayResources(cost));
    }

    @Test
    public void payResourceCost() {
        Map<Resource,Integer> cost=new HashMap<>();
        cost.put(Resource.COIN, 8);
        playerBoard.payResourceCost(cost);

        Map<Resource, Integer> strongboxResources=new HashMap<>();
        strongboxResources.put(Resource.SERVANT, 12);
        strongboxResources.put(Resource.COIN, 2);
        strongboxResources.put(Resource.STONE, 9);
        strongboxResources.put(Resource.SHIELD, 15);

        Map<Resource, Integer> bottomResources=new HashMap<>();
        bottomResources.put(Resource.SERVANT, 0);
        bottomResources.put(Resource.COIN, 0);
        bottomResources.put(Resource.STONE, 0);
        bottomResources.put(Resource.SHIELD, 0);

        assertEquals(strongboxResources, playerBoard.getStrongbox().getResources());
        assertEquals(bottomResources, playerBoard.getWarehouse().getShelfResources("bottom"));
        assertEquals(bottomResources, playerBoard.getWarehouse().getShelfResources("extra1"));
    }
}