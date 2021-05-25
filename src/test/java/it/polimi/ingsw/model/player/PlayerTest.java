package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    @Before
    public void setUp() {
        player=new Player("prova");
    }

    @Test
    public void canPlayLeader() {

        Map<Resource,Integer> resourceRequirements=new HashMap<>();
        Map<Resource, Integer> add_res=new HashMap<>();
        ArrayList<LeaderCard> testCards = new ArrayList<>();

        Map<Resource, Integer> emptyMap1 = new HashMap<>();
        Map<CardType,Integer> emptyMap2=new HashMap<>();


        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);

        player.getPlayerBoard().getStrongbox().addResources(add_res);

        //Leader card for test resourceRequirements from strongbox
        resourceRequirements.put(Resource.SERVANT, 5);
        resourceRequirements.put(Resource.COIN, 3);
        resourceRequirements.put(Resource.STONE, 2);
        resourceRequirements.put(Resource.SHIELD, 10);


        LeaderCard leaderCardTest=new LeaderCard(resourceRequirements, emptyMap2, 0, "Market",
                                                    Resource.COIN, 0);
        assertEquals(0,leaderCardTest.getScore());
        assertEquals("Market",leaderCardTest.getEffectScope());
        assertEquals(Resource.COIN,leaderCardTest.getEffectObject());
        assertEquals("Resource requirements: "+resourceRequirements+"\n",leaderCardTest.printResourceRequirements());
        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertTrue(player.canPlayLeader(0));

        resourceRequirements.put(Resource.SERVANT, 13);
        resourceRequirements.put(Resource.COIN, 3);
        resourceRequirements.put(Resource.STONE, 2);
        resourceRequirements.put(Resource.SHIELD, 10);


        leaderCardTest=new LeaderCard(resourceRequirements, emptyMap2, 0, "Market",
                                    Resource.COIN, 0);

        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertFalse(player.canPlayLeader(1));

        //Leader card for test resourceRequirements from warehouse
        player.getPlayerBoard().getStrongbox().useResources(add_res);
        Shelf shel3ftest=player.getPlayerBoard().getWarehouse().getShelf("bottom");

        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,3);
        shel3ftest.addResources(shelfRes);

        resourceRequirements.put(Resource.SERVANT, 0);
        resourceRequirements.put(Resource.COIN, 3);
        resourceRequirements.put(Resource.STONE, 0);
        resourceRequirements.put(Resource.SHIELD, 0);

        leaderCardTest=new LeaderCard(resourceRequirements, emptyMap2, 0, "Market",
                Resource.COIN, 0);
       testCards.add(leaderCardTest);
       player.setLeaderCards(testCards);
       assertTrue(player.canPlayLeader(2));


        resourceRequirements.put(Resource.SERVANT, 0);
        resourceRequirements.put(Resource.COIN, 0);
        resourceRequirements.put(Resource.STONE, 5);
        resourceRequirements.put(Resource.SHIELD, 0);

        leaderCardTest=new LeaderCard(resourceRequirements, emptyMap2, 0, "Market",
                Resource.COIN, 0);
        testCards.add(leaderCardTest);
        assertFalse(player.canPlayLeader(3));

        //Leader cards for test cardRequirements


        ProductionPower productionPower=new ProductionPower(emptyMap1,emptyMap1);

        DevelopmentCard developmentCard1=new DevelopmentCard(1,CardType.GREEN, emptyMap1,
                                                            productionPower,3);
        DevelopmentCard developmentCard2=new DevelopmentCard(1,CardType.GREEN, emptyMap1,
                                                            productionPower,3);
        DevelopmentCard developmentCard3=new DevelopmentCard(2,CardType.BLUE, emptyMap1,
                                                            productionPower,3);
        DevelopmentCard developmentCard4=new DevelopmentCard(3,CardType.PURPLE, emptyMap1,
                                                            productionPower,3);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCard1);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCard2);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCard3);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCard4);




        Map<CardType,Integer> cardRequirements=new HashMap<>();
        cardRequirements.put(CardType.GREEN,2);
        cardRequirements.put(CardType.BLUE,1);
        cardRequirements.put(CardType.PURPLE,1);


        leaderCardTest=new LeaderCard(emptyMap1, cardRequirements, 0, "Market",
                Resource.COIN, 0);
        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertEquals("Card requirements: "+cardRequirements+" Level: "+ leaderCardTest.getCardRequirementsLevel()+"\n",
                leaderCardTest.printCardRequirements());
        assertEquals("\n"+leaderCardTest.printResourceRequirements()+leaderCardTest.printCardRequirements()+
                "Score: "+leaderCardTest.getScore()+"\n"+"Effect: "+leaderCardTest.getEffectScope()+
                " "+leaderCardTest.getEffectObject()+"\n",leaderCardTest.toString());
        assertTrue(player.canPlayLeader(4));



        developmentCard1=new DevelopmentCard(1,CardType.GREEN, emptyMap1,
                productionPower,3);
        developmentCard2=new DevelopmentCard(2,CardType.GREEN, emptyMap1,
                productionPower,3);
        developmentCard3=new DevelopmentCard(3,CardType.GREEN, emptyMap1,
                productionPower,3);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard2);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard3);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCard2);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCard3);



        cardRequirements=new HashMap<>();
        cardRequirements.put(CardType.GREEN,2);
        cardRequirements.put(CardType.BLUE,1);
        cardRequirements.put(CardType.PURPLE,1);


        leaderCardTest=new LeaderCard(emptyMap1, cardRequirements, 0, "Market",
                Resource.COIN, 0);
        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertTrue(player.canPlayLeader(5));

        //test for cardRequirementsLevel
        cardRequirements=new HashMap<>();
        cardRequirements.put(CardType.GREEN,2);
        cardRequirements.put(CardType.PURPLE,1);

        leaderCardTest=new LeaderCard(emptyMap1, cardRequirements, 0, "Market",
                Resource.COIN, 3);

        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertTrue(player.canPlayLeader(6));

        cardRequirements=new HashMap<>();
        cardRequirements.put(CardType.BLUE,2);
        cardRequirements.put(CardType.PURPLE,1);

        leaderCardTest=new LeaderCard(emptyMap1, cardRequirements, 0, "Market",
                Resource.COIN, 3);

        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertFalse(player.canPlayLeader(7));

        player=new Player("prova");
        cardRequirements=new HashMap<>();
        cardRequirements.put(CardType.PURPLE,1);
        leaderCardTest=new LeaderCard(emptyMap1,cardRequirements,0,"Production",Resource.STONE,2);

        developmentCard1=new DevelopmentCard(1,CardType.PURPLE, emptyMap1,
                productionPower,3);
        developmentCard2=new DevelopmentCard(2,CardType.PURPLE, emptyMap1,
                productionPower,2);

        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCard2);
        testCards.add(leaderCardTest);
        player.setLeaderCards(testCards);
        assertTrue(player.canPlayLeader(8));
    }

    @Test
    public void canBuyAndPlaceDevelopmentCard(){
        Map<Resource,Integer> cost=new HashMap<>();
        Map<Resource, Integer> add_res=new HashMap<>();

        Map<Resource, Integer> emptyMap1 = new HashMap<>();

        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);

        player.getPlayerBoard().getStrongbox().addResources(add_res);

        //Development card for test resourceRequirements from strongbox
        cost.put(Resource.SERVANT, 5);
        cost.put(Resource.COIN, 3);
        cost.put(Resource.STONE, 2);
        cost.put(Resource.SHIELD, 10);

        ProductionPower productionPower=new ProductionPower(emptyMap1,emptyMap1);
        DevelopmentCard developmentCardTest=new DevelopmentCard(1,CardType.GREEN,cost,productionPower,0);
        assertTrue(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));
        assertEquals(0,developmentCardTest.getScore());

        cost.put(Resource.SERVANT, 13); //Strongbox has only 12 Servant...
        cost.put(Resource.COIN, 3);
        cost.put(Resource.STONE, 2);
        cost.put(Resource.SHIELD, 10);


        developmentCardTest=new DevelopmentCard(1,CardType.GREEN,cost,productionPower,0);
        assertFalse(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));



//       resources from warehouse
        //clean strongbox
        player.getPlayerBoard().getStrongbox().useResources(add_res);

        Shelf shel3ftest=player.getPlayerBoard().getWarehouse().getShelf("bottom");

        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,3);
        shel3ftest.addResources(shelfRes);

        cost.put(Resource.SERVANT, 0);
        cost.put(Resource.COIN, 3);
        cost.put(Resource.STONE, 0);
        cost.put(Resource.SHIELD, 0);

        developmentCardTest=new DevelopmentCard(1,CardType.GREEN,cost,productionPower,0);

        assertTrue(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));

        cost.put(Resource.SERVANT, 0);
        cost.put(Resource.COIN, 0);
        cost.put(Resource.STONE, 5);
        cost.put(Resource.SHIELD, 0);


        developmentCardTest=new DevelopmentCard(1,CardType.GREEN,cost,productionPower,0);
        assertFalse(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));

//        get resources for buy development card from strongbox and warehouse
        add_res.put(Resource.SERVANT, 0);
        add_res.put(Resource.COIN, 5);
        add_res.put(Resource.STONE, 5);
        add_res.put(Resource.SHIELD, 0);

        player.getPlayerBoard().getStrongbox().addResources(add_res);

        cost.put(Resource.SERVANT, 0);
        cost.put(Resource.COIN, 8);
        cost.put(Resource.STONE, 5);
        cost.put(Resource.SHIELD, 0);

        developmentCardTest=new DevelopmentCard(1,CardType.GREEN,cost,productionPower,0);
        assertTrue(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));

        cost.put(Resource.SERVANT, 0);
        cost.put(Resource.COIN, 9);
        cost.put(Resource.STONE, 5);
        cost.put(Resource.SHIELD, 0);

        developmentCardTest=new DevelopmentCard(1,CardType.GREEN,cost,productionPower,0);

        assertFalse(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));
//        test if I can place card

        cost.put(Resource.SERVANT, 0);
        cost.put(Resource.COIN, 8);
        cost.put(Resource.STONE, 5);
        cost.put(Resource.SHIELD, 0);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest);
        developmentCardTest=new DevelopmentCard(2,CardType.GREEN,cost,productionPower,0);

        assertTrue(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));

        developmentCardTest=new DevelopmentCard(3,CardType.GREEN,cost,productionPower,0);

        assertFalse(player.canBuyAndPlaceDevelopmentCard(developmentCardTest));
    }

    @Test
    public void canActivateProduction(){
        Map<Resource,Integer>  input=new HashMap<>();
        Map<Resource, Integer> add_res=new HashMap<>();

        Map<Resource, Integer> emptyMap1 = new HashMap<>();

        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);
        player.getPlayerBoard().getStrongbox().addResources(add_res);

        input.put(Resource.SERVANT, 12);
        input.put(Resource.COIN, 6);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        ProductionPower productionPower=new ProductionPower(input, emptyMap1);

        assertFalse(player.canActivateProduction());


        DevelopmentCard developmentCardTest1=new DevelopmentCard(1,CardType.GREEN,emptyMap1,productionPower,0);
        DevelopmentCard developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
        DevelopmentCard developmentCardTest3=new DevelopmentCard(3,CardType.BLUE,emptyMap1,productionPower,0);

        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest3);

        assertTrue(player.canActivateProduction());

        developmentCardTest1=new DevelopmentCard(1,CardType.YELLOW,emptyMap1,productionPower,0);
        developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
        developmentCardTest3=new DevelopmentCard(3,CardType.GREEN,emptyMap1,productionPower,0);

        player.getPlayerBoard().getStrongbox().useResources(add_res);

        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest3);

        assertFalse(player.canActivateProduction());

        input.put(Resource.SERVANT, 0);
        input.put(Resource.COIN, 2);
        input.put(Resource.STONE, 0);
        input.put(Resource.SHIELD, 0);

        productionPower=new ProductionPower(input, emptyMap1);

        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,3);
        player.getPlayerBoard().getWarehouse().getShelf("bottom").addResources(shelfRes);

        developmentCardTest1=new DevelopmentCard(1,CardType.YELLOW,emptyMap1,productionPower,0);
        developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
        developmentCardTest3=new DevelopmentCard(3,CardType.GREEN,emptyMap1,productionPower,0);

        player.getPlayerBoard().getCardStacks().get(2).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCardTest3);

        assertTrue(player.canActivateProduction());

        player.getPlayerBoard().getWarehouse().getShelf("bottom").useResources(shelfRes);

        assertFalse(player.canActivateProduction());

        player.getPlayerBoard().getWarehouse().getShelf("bottom").addResources(shelfRes);

        player.getPlayerBoard().getCardStacks().get(2).pop();
        player.getPlayerBoard().getCardStacks().get(2).pop();
        player.getPlayerBoard().getCardStacks().get(2).pop();

        player.getPlayerBoard().getCardStacks().get(1).pop();
        player.getPlayerBoard().getCardStacks().get(1).pop();
        player.getPlayerBoard().getCardStacks().get(1).pop();

        player.getPlayerBoard().getCardStacks().get(0).pop();
        player.getPlayerBoard().getCardStacks().get(0).pop();
        player.getPlayerBoard().getCardStacks().get(0).pop();

        player.getPlayerBoard().getWarehouse().getShelf("bottom").addResources(shelfRes);
        player.getPlayerBoard().getStrongbox().addResources(add_res);


        input.put(Resource.SERVANT, 12);
        input.put(Resource.COIN, 10);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        productionPower=new ProductionPower(input, emptyMap1);


        developmentCardTest1=new DevelopmentCard(1,CardType.YELLOW,emptyMap1,productionPower,0);
        developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
        developmentCardTest3=new DevelopmentCard(3,CardType.GREEN,emptyMap1,productionPower,0);

        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest3);

        assertFalse(player.canActivateProduction());


        input.put(Resource.SERVANT, 12);
        input.put(Resource.COIN, 9);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        productionPower=new ProductionPower(input, emptyMap1);


        developmentCardTest1=new DevelopmentCard(1,CardType.YELLOW,emptyMap1,productionPower,0);
        developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
        developmentCardTest3=new DevelopmentCard(3,CardType.GREEN,emptyMap1,productionPower,0);

        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest3);

        assertTrue(player.canActivateProduction());
    }

    @Test
    public void possibleDevelopmentCardProduction(){
        Map<Resource,Integer>  input=new HashMap<>();
        Map<Resource, Integer> add_res=new HashMap<>();

        Map<Resource, Integer> emptyMap1 = new HashMap<>();

        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);
        player.getPlayerBoard().getStrongbox().addResources(add_res);

//      Cards that player can active
        input.put(Resource.SERVANT, 12);
        input.put(Resource.COIN, 6);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        ProductionPower productionPower=new ProductionPower(input, emptyMap1);

        assertFalse(player.canActivateProduction());

//        card to active
        DevelopmentCard developmentCardTest1=new DevelopmentCard(1,CardType.GREEN,emptyMap1,productionPower,0);
        DevelopmentCard developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
//        card to active
        DevelopmentCard developmentCardTest3=new DevelopmentCard(1,CardType.BLUE,emptyMap1,productionPower,0);

        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest3);

//      Cards that player can not active
        ArrayList<DevelopmentCard> expected=new ArrayList<>();

        input=new HashMap<>();

        input.put(Resource.SERVANT, 15);
        input.put(Resource.COIN, 6);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        productionPower=new ProductionPower(input, emptyMap1);

//      card not to active
        DevelopmentCard developmentCardTest4=new DevelopmentCard(1,CardType.PURPLE,emptyMap1,productionPower,0);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCardTest4);


//      Player can active only developmentCard2 and developmentCard3
        expected.add(developmentCardTest2);
        expected.add(developmentCardTest3);

        assertEquals(expected, player.possibleDevelopmentCardProduction());

//        add resources on one shelf for active developmentCard4

        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.SERVANT,3);
        player.getPlayerBoard().getWarehouse().getShelf("bottom").addResources(shelfRes);

        expected.add(developmentCardTest4);
        assertEquals(expected, player.possibleDevelopmentCardProduction());
    }

    @Test
    public void possibleProductionPowersToActive()
    {
        Map<Resource,Integer>  input=new HashMap<>();
        Map<Resource, Integer> add_res=new HashMap<>();

        Map<Resource, Integer> emptyMap1 = new HashMap<>();

        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);
        player.getPlayerBoard().getStrongbox().addResources(add_res);

//      Cards that player can active
        input.put(Resource.SERVANT, 12);
        input.put(Resource.COIN, 6);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        ProductionPower productionPower=new ProductionPower(input, emptyMap1);

        assertFalse(player.canActivateProduction());

//        card to active
        DevelopmentCard developmentCardTest1=new DevelopmentCard(1,CardType.GREEN,emptyMap1,productionPower,0);
        DevelopmentCard developmentCardTest2=new DevelopmentCard(2,CardType.PURPLE,emptyMap1,productionPower,0);
//        card to active
        DevelopmentCard developmentCardTest3=new DevelopmentCard(1,CardType.BLUE,emptyMap1,productionPower,0);

        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest1);
        player.getPlayerBoard().getCardStacks().get(0).push(developmentCardTest2);
        player.getPlayerBoard().getCardStacks().get(1).push(developmentCardTest3);

//      Cards that player can not active
        ArrayList<ProductionPower> expected=new ArrayList<>();

        input=new HashMap<>();

        input.put(Resource.SERVANT, 15);
        input.put(Resource.COIN, 6);
        input.put(Resource.STONE, 9);
        input.put(Resource.SHIELD, 15);

        productionPower=new ProductionPower(input, emptyMap1);

//      card not to active
        DevelopmentCard developmentCardTest4=new DevelopmentCard(1,CardType.PURPLE,emptyMap1,productionPower,0);
        player.getPlayerBoard().getCardStacks().get(2).push(developmentCardTest4);


//      Player can active only developmentCard2 and developmentCard3
        expected.add(developmentCardTest2.getProductionPower());
        expected.add(developmentCardTest3.getProductionPower());

        assertEquals(expected, player.possibleProductionPowersToActive());

//        add resources on one shelf for active developmentCard4

        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.SERVANT,3);
        player.getPlayerBoard().getWarehouse().getShelf("bottom").addResources(shelfRes);

        expected.add(developmentCardTest4.getProductionPower());
        assertEquals(expected, player.possibleProductionPowersToActive());
    }

    @Test
    public void canActivateBasicProduction(){
        assertFalse(player.canActivateBasicProduction());
        Map<Resource,Integer> shelfRes=new HashMap<>();
        shelfRes.put(Resource.SERVANT,1);
        player.getPlayerBoard().getWarehouse().getShelf("top").addResources(shelfRes);
        assertFalse(player.canActivateBasicProduction());

        shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,1);
        player.getPlayerBoard().getWarehouse().getShelf("middle").addResources(shelfRes);
        assertTrue(player.canActivateBasicProduction());

        shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,3);
        player.getPlayerBoard().getWarehouse().getShelf("bottom").addResources(shelfRes);
        assertTrue(player.canActivateBasicProduction());


        shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,3);
        player.getPlayerBoard().getWarehouse().getShelf("bottom").useResources(shelfRes);

        shelfRes=new HashMap<>();
        shelfRes.put(Resource.COIN,1);
        player.getPlayerBoard().getWarehouse().getShelf("middle").useResources(shelfRes);

        assertFalse(player.canActivateBasicProduction());
    }

}