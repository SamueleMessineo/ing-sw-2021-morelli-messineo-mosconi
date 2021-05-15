package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.utils.GameUtils;
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
//        Test Strong Box is empty && Warehouse is not empty
        playerBoard=new PlayerBoard();
        List<Integer> cardsStackToActive=new ArrayList<>();
        Map<Resource,Integer> input1= GameUtils.emptyResourceMap();
        input1.put(Resource.SERVANT,1);
        input1.put(Resource.COIN,2);
        input1.put(Resource.SHIELD,3);
        input1.put(Resource.STONE,0);

        Map<Resource,Integer> warehouseRes= new HashMap<>(input1);
        playerBoard.getWarehouse().placeResources(warehouseRes);
        assertEquals(input1,playerBoard.getWarehouse().getResources());

        Map<Resource,Integer> output1=GameUtils.emptyResourceMap();
        output1.put(Resource.SERVANT,3);
        output1.put(Resource.COIN,3);
        output1.put(Resource.SHIELD,3);
        output1.put(Resource.STONE,3);

        ProductionPower productionPower=new ProductionPower(input1,output1);

        DevelopmentCard developmentCard1=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(0).push(developmentCard1);
        cardsStackToActive.add(0);

        playerBoard.activateProduction(cardsStackToActive);
        assertEquals(output1,playerBoard.getStrongbox().getResources());
        assertEquals(GameUtils.emptyResourceMap(),playerBoard.getWarehouse().getResources());
        assertEquals(output1,playerBoard.getResources());

//        Test Strong Box is empty && Warehouse is not empty
        playerBoard=new PlayerBoard();
        warehouseRes.put(Resource.SERVANT,1);
        warehouseRes.put(Resource.COIN,2);
        warehouseRes.put(Resource.SHIELD,3);
        warehouseRes.put(Resource.STONE,0);
        playerBoard.getWarehouse().placeResources(warehouseRes);
        assertEquals(input1,playerBoard.getWarehouse().getResources());

        cardsStackToActive=new ArrayList<>();
        input1= GameUtils.emptyResourceMap();
        input1.put(Resource.SERVANT,1);
        input1.put(Resource.COIN,0);
        input1.put(Resource.SHIELD,3);
        input1.put(Resource.STONE,0);

        output1=GameUtils.emptyResourceMap();
        output1.put(Resource.SERVANT,3);
        output1.put(Resource.COIN,3);
        output1.put(Resource.SHIELD,3);
        output1.put(Resource.STONE,3);

        productionPower=new ProductionPower(input1,output1);

        developmentCard1=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(0).push(developmentCard1);
        cardsStackToActive.add(0);

        Map<Resource, Integer> input2= GameUtils.emptyResourceMap();
        input2.put(Resource.SERVANT,0);
        input2.put(Resource.COIN,2);
        input2.put(Resource.SHIELD,0);
        input2.put(Resource.STONE,0);

        Map<Resource, Integer> output2=GameUtils.emptyResourceMap();
        output2.put(Resource.SERVANT,0);
        output2.put(Resource.COIN,3);
        output2.put(Resource.SHIELD,0);
        output2.put(Resource.STONE,3);

        productionPower=new ProductionPower(input2,output2);

        DevelopmentCard developmentCard2=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(1).push(developmentCard2);
        cardsStackToActive.add(1);

        playerBoard.activateProduction(cardsStackToActive);
        assertEquals(GameUtils.sumResourcesMaps(output1,output2),playerBoard.getStrongbox().getResources());
        assertEquals(GameUtils.emptyResourceMap(),playerBoard.getWarehouse().getResources());
        assertEquals(GameUtils.sumResourcesMaps(output1,output2),playerBoard.getResources());

//        Test Strong Box is empty && Warehouse is not empty
        playerBoard=new PlayerBoard();
        warehouseRes.put(Resource.SERVANT,1);
        warehouseRes.put(Resource.COIN,2);
        warehouseRes.put(Resource.SHIELD,3);
        warehouseRes.put(Resource.STONE,0);
        playerBoard.getWarehouse().placeResources(warehouseRes);
        assertEquals(warehouseRes,playerBoard.getWarehouse().getResources());

        cardsStackToActive=new ArrayList<>();
        input1= GameUtils.emptyResourceMap();
        input1.put(Resource.SERVANT,1);
        input1.put(Resource.COIN,0);
        input1.put(Resource.SHIELD,0);
        input1.put(Resource.STONE,0);

        output1=GameUtils.emptyResourceMap();
        output1.put(Resource.SERVANT,3);
        output1.put(Resource.COIN,3);
        output1.put(Resource.SHIELD,3);
        output1.put(Resource.STONE,3);

        productionPower=new ProductionPower(input1,output1);

        developmentCard1=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(0).push(developmentCard1);
        cardsStackToActive.add(0);

        input2= GameUtils.emptyResourceMap();
        input2.put(Resource.SERVANT,0);
        input2.put(Resource.COIN,2);
        input2.put(Resource.SHIELD,0);
        input2.put(Resource.STONE,0);

        output2=GameUtils.emptyResourceMap();
        output2.put(Resource.SERVANT,0);
        output2.put(Resource.COIN,3);
        output2.put(Resource.SHIELD,0);
        output2.put(Resource.STONE,3);

        productionPower=new ProductionPower(input2,output2);

        developmentCard2=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(1).push(developmentCard2);
        cardsStackToActive.add(1);

        Map<Resource, Integer> input3= GameUtils.emptyResourceMap();
        input3.put(Resource.SERVANT,0);
        input3.put(Resource.COIN,0);
        input3.put(Resource.SHIELD,3);
        input3.put(Resource.STONE,0);

        Map<Resource, Integer> output3= GameUtils.emptyResourceMap();
        output3.put(Resource.SERVANT,0);
        output3.put(Resource.COIN,1);
        output3.put(Resource.SHIELD,6);
        output3.put(Resource.STONE,2);

        productionPower=new ProductionPower(input3,output3);

        DevelopmentCard developmentCard3=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(2).push(developmentCard3);
        cardsStackToActive.add(2);

        playerBoard.activateProduction(cardsStackToActive);
        assertEquals(GameUtils.sumResourcesMaps(GameUtils.sumResourcesMaps(output1,output2),output3),playerBoard.getStrongbox().getResources());
        assertEquals(GameUtils.emptyResourceMap(),playerBoard.getWarehouse().getResources());
        assertEquals(GameUtils.sumResourcesMaps(GameUtils.sumResourcesMaps(output1,output2),output3),playerBoard.getResources());

//        Test Strong Box is not empty && Warehouse is not empty
        playerBoard=new PlayerBoard();
        warehouseRes.put(Resource.SERVANT,1);
        warehouseRes.put(Resource.COIN,2);
        warehouseRes.put(Resource.SHIELD,3);
        warehouseRes.put(Resource.STONE,0);
        playerBoard.getWarehouse().placeResources(warehouseRes);
        assertEquals(warehouseRes,playerBoard.getWarehouse().getResources());

        Map<Resource,Integer> strongboxRes=GameUtils.emptyResourceMap();
        strongboxRes.put(Resource.SERVANT,5);
        strongboxRes.put(Resource.COIN,42);
        strongboxRes.put(Resource.SHIELD,28);
        strongboxRes.put(Resource.STONE,37);
        playerBoard.getStrongbox().addResources(strongboxRes);
        assertEquals(strongboxRes,playerBoard.getStrongbox().getResources());

        cardsStackToActive=new ArrayList<>();
        input1= GameUtils.emptyResourceMap();
        input1.put(Resource.SERVANT,6);
        input1.put(Resource.COIN,0);
        input1.put(Resource.SHIELD,17);
        input1.put(Resource.STONE,0);

        output1=GameUtils.emptyResourceMap();
        output1.put(Resource.SERVANT,3);
        output1.put(Resource.COIN,3);
        output1.put(Resource.SHIELD,3);
        output1.put(Resource.STONE,3);

        productionPower=new ProductionPower(input1,output1);

        developmentCard1=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(0).push(developmentCard1);
        cardsStackToActive.add(0);

        input2= GameUtils.emptyResourceMap();
        input2.put(Resource.SERVANT,0);
        input2.put(Resource.COIN,23);
        input2.put(Resource.SHIELD,14);
        input2.put(Resource.STONE,0);

        output2=GameUtils.emptyResourceMap();
        output2.put(Resource.SERVANT,0);
        output2.put(Resource.COIN,3);
        output2.put(Resource.SHIELD,0);
        output2.put(Resource.STONE,3);

        productionPower=new ProductionPower(input2,output2);

        developmentCard2=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(1).push(developmentCard2);
        cardsStackToActive.add(1);

        input3= GameUtils.emptyResourceMap();
        input3.put(Resource.SERVANT,0);
        input3.put(Resource.COIN,21);
        input3.put(Resource.SHIELD,0);
        input3.put(Resource.STONE,37);

        output3= GameUtils.emptyResourceMap();
        output3.put(Resource.SERVANT,0);
        output3.put(Resource.COIN,1);
        output3.put(Resource.SHIELD,6);
        output3.put(Resource.STONE,2);

        productionPower=new ProductionPower(input3,output3);

        developmentCard3=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(2).push(developmentCard3);
        cardsStackToActive.add(2);

        playerBoard.activateProduction(cardsStackToActive);

        assertEquals(GameUtils.sumResourcesMaps(GameUtils.sumResourcesMaps(output1,output2),output3),playerBoard.getStrongbox().getResources());
        assertEquals(GameUtils.emptyResourceMap(),playerBoard.getWarehouse().getResources());
        assertEquals(GameUtils.sumResourcesMaps(GameUtils.sumResourcesMaps(output1,output2),output3),playerBoard.getResources());

//        Test Strong Box is not empty && Warehouse is not empty
        playerBoard=new PlayerBoard();
        warehouseRes.put(Resource.SERVANT,1);
        warehouseRes.put(Resource.COIN,2);
        warehouseRes.put(Resource.SHIELD,3);
        warehouseRes.put(Resource.STONE,0);
        playerBoard.getWarehouse().placeResources(warehouseRes);
        assertEquals(warehouseRes,playerBoard.getWarehouse().getResources());

        strongboxRes=GameUtils.emptyResourceMap();
        strongboxRes.put(Resource.SERVANT,50);
        strongboxRes.put(Resource.COIN,50);
        strongboxRes.put(Resource.SHIELD,50);
        strongboxRes.put(Resource.STONE,50);
        playerBoard.getStrongbox().addResources(strongboxRes);
        assertEquals(strongboxRes,playerBoard.getStrongbox().getResources());

        cardsStackToActive=new ArrayList<>();
        input1= GameUtils.emptyResourceMap();
        input1.put(Resource.SERVANT,1);
        input1.put(Resource.COIN,0);
        input1.put(Resource.SHIELD,3);
        input1.put(Resource.STONE,0);

        output1=GameUtils.emptyResourceMap();
        output1.put(Resource.SERVANT,25);
        output1.put(Resource.COIN,25);
        output1.put(Resource.SHIELD,0);
        output1.put(Resource.STONE,0);

        productionPower=new ProductionPower(input1,output1);

        developmentCard1=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(0).push(developmentCard1);
        cardsStackToActive.add(0);

        input2= GameUtils.emptyResourceMap();
        input2.put(Resource.SERVANT,0);
        input2.put(Resource.COIN,0);
        input2.put(Resource.SHIELD,25);
        input2.put(Resource.STONE,25);

        output2=GameUtils.emptyResourceMap();
        output2.put(Resource.SERVANT,25);
        output2.put(Resource.COIN,25);
        output2.put(Resource.SHIELD,0);
        output2.put(Resource.STONE,0);

        productionPower=new ProductionPower(input2,output2);

        developmentCard2=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(1).push(developmentCard2);
        cardsStackToActive.add(1);

        input3= GameUtils.emptyResourceMap();
        input3.put(Resource.SERVANT,0);
        input3.put(Resource.COIN,0);
        input3.put(Resource.SHIELD,25);
        input3.put(Resource.STONE,25);

        output3= GameUtils.emptyResourceMap();
        output3.put(Resource.SERVANT,1);
        output3.put(Resource.COIN,1);
        output3.put(Resource.SHIELD,0);
        output3.put(Resource.STONE,0);

        productionPower=new ProductionPower(input3,output3);

        developmentCard3=new DevelopmentCard(1, CardType.PURPLE, new HashMap<Resource,Integer>(), productionPower,0);
        playerBoard.getCardStacks().get(2).push(developmentCard3);
        cardsStackToActive.add(2);

        playerBoard.activateProduction(cardsStackToActive);

        Map<Resource,Integer> strongboxResourceIntegerMap= GameUtils.emptyResourceMap();
        strongboxResourceIntegerMap.put(Resource.SERVANT,101);
        strongboxResourceIntegerMap.put(Resource.COIN,101);
        strongboxResourceIntegerMap.put(Resource.SHIELD,0);
        strongboxResourceIntegerMap.put(Resource.STONE,0);

        Map<Resource,Integer> warehouseResourceIntegerMap= GameUtils.emptyResourceMap();
        warehouseResourceIntegerMap.put(Resource.SERVANT,0);
        warehouseResourceIntegerMap.put(Resource.COIN,2);
        warehouseResourceIntegerMap.put(Resource.SHIELD,0);
        warehouseResourceIntegerMap.put(Resource.STONE,0);

        assertEquals(strongboxResourceIntegerMap,playerBoard.getStrongbox().getResources());
        assertEquals(warehouseResourceIntegerMap,playerBoard.getWarehouse().getResources());
        assertEquals(GameUtils.sumResourcesMaps(warehouseResourceIntegerMap,strongboxResourceIntegerMap),playerBoard.getResources());
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