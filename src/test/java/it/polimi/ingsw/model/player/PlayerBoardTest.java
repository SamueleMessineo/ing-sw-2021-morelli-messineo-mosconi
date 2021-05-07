package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
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
    }

    @Test
    public void canPlaceDevelopmentCard() {
    }

    @Test
    public void canPayResources() {
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