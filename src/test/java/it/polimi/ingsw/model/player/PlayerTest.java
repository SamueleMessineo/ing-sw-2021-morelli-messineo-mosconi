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
        assertFalse(player.canPlayLeader(5));

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
    }
}