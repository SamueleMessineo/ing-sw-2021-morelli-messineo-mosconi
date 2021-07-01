package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.shared.*;
import it.polimi.ingsw.utils.GameUtils;
import it.polimi.ingsw.view.Display;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.Assert.*;

public class ClassicGameControllerTest {
    private Game game1;
    private ClassicGameController gameController;

    @Before
    public void setup() throws FileNotFoundException {
        String path = "src/main/resources/testGames/0001.ser";
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game1 = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        for(Player p:game1.getPlayers()){
            p.setActive(true);
        }
        gameController= new ClassicGameController(game1);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void selectStartingPlayerTest(){
        gameController.startGame();
        assertTrue(game1.getActivePlayers().contains(game1.getCurrentPlayer()));
    }

    @Test
    public void dropInitialLeaderCards(){
        gameController.startGame();
        game1.getCurrentPlayer().dropInitialLeaderCards(0,1);
        assertEquals(new ArrayList<LeaderCard>(),game1.getCurrentPlayer().getLeaderCards());
    }

    @Test
    public void dropInitialLeaderCardsTest(){
        gameController.startGame();
        game1.getCurrentPlayer().dropInitialLeaderCards(0,1);
        assertEquals(new ArrayList<LeaderCard>(),game1.getCurrentPlayer().getLeaderCards());
    }

    @Test
    public void isGameOverTest1(){
        gameController.startGame();
        assertFalse(gameController.isGameOver());
        for(int i=0; i<=23; i++)
        {
            game1.getCurrentPlayer().getFaithTrack().move();
        }
        assertTrue(gameController.isGameOver());
    }

    @Test
    public void isGameOverTest2(){
        gameController.startGame();
        assertFalse(gameController.isGameOver());
        List<PlayerCardStack> cardStacks=game1.getCurrentPlayer().getPlayerBoard().getCardStacks();
        DevelopmentCard developmentCard1=new DevelopmentCard(1,
                CardType.BLUE,GameUtils.emptyResourceMap(),
                new ProductionPower(GameUtils.emptyResourceMap(),GameUtils.emptyResourceMap()),
                0);
        DevelopmentCard developmentCard2=new DevelopmentCard(2,
                CardType.BLUE,GameUtils.emptyResourceMap(),
                new ProductionPower(GameUtils.emptyResourceMap(),GameUtils.emptyResourceMap()),
                0);
        DevelopmentCard developmentCard3=new DevelopmentCard(3,
                CardType.BLUE,GameUtils.emptyResourceMap(),
                new ProductionPower(GameUtils.emptyResourceMap(),GameUtils.emptyResourceMap()),
                0);
        PlayerCardStack playerCardStack=new PlayerCardStack();
        playerCardStack.push(developmentCard1);
        playerCardStack.push(developmentCard2);
        playerCardStack.push(developmentCard3);
        for (int i =0; i<3; i++){
            cardStacks.remove(0);
            cardStacks.add(playerCardStack);
        }
        assertTrue(gameController.isGameOver());
    }

    @Test
    public void computeNextPlayerTest(){
        gameController.startGame();
        List<Player> activePlayers=new ArrayList<>(game1.getActivePlayers());
        Player nextPlayer= activePlayers.get((game1.getActivePlayers().indexOf(game1.getCurrentPlayer()) + 1) % game1.getActivePlayers().size());
        gameController.computeNextPlayer();
        assertEquals(nextPlayer,game1.getCurrentPlayer());
    }

    @Test
    public void getMarblesTest(){
        MarbleStructure marbleStructure=new MarbleStructure(
                new ArrayList<Marble>(game1.getMarket().getMarbleStructure().getMarbles()),
                game1.getMarket().getMarbleStructure().getExtraMarble());
        assertEquals(marbleStructure.getExtraMarble(), game1.getMarket().getMarbleStructure().getExtraMarble());
        assertEquals(marbleStructure.getMarbles(), game1.getMarket().getMarbleStructure().getMarbles());


        int index=2;
        marbleStructure.shiftRow(index);
        gameController.getMarbles("ROW",index);
        assertEquals(marbleStructure.getMarbles(), game1.getMarket().getMarbleStructure().getMarbles());
        assertEquals(marbleStructure.getExtraMarble(), game1.getMarket().getMarbleStructure().getExtraMarble());


        index=2;
        marbleStructure.shiftColumn(index);
        gameController.getMarbles("COLUMN",index);
        assertEquals(marbleStructure.getMarbles(), game1.getMarket().getMarbleStructure().getMarbles());
        assertEquals(marbleStructure.getExtraMarble(), game1.getMarket().getMarbleStructure().getExtraMarble());
    }

    @Test
    public void dropPlayerResources() {
        Map<Resource,Integer> obtainedResources= new HashMap<>();
        Map<Resource,Integer> resourcesToDrop= new HashMap<>();
        Warehouse warehouse=game1.getCurrentPlayer().getPlayerBoard().getWarehouse();
        List<Player> other=game1.getActivePlayers();
        other.remove(game1.getCurrentPlayer());
        Map<Player,Integer> mapPosition=new HashMap<>();

        for (Player player:other){
            mapPosition.put(player,player.getFaithTrack().getPosition());
        }

        for(Shelf shelf: warehouse.getShelves()){
            shelf.useResources(shelf.getResources());
        }

        obtainedResources.put(Resource.STONE, 3);
        obtainedResources.put(Resource.COIN, 5);
        obtainedResources.put(Resource.SERVANT, 5);
        obtainedResources.put(Resource.SHIELD, 5);

        resourcesToDrop.put(Resource.STONE,4);
        exceptionRule.expect(InvalidParameterException.class);
        gameController.dropPlayerResources(obtainedResources,resourcesToDrop,
                game1.getCurrentPlayer().getUsername());

        resourcesToDrop.put(Resource.STONE,3);
        exceptionRule.expect(InvalidParameterException.class);
        gameController.dropPlayerResources(obtainedResources,resourcesToDrop,
                game1.getCurrentPlayer().getUsername());


        obtainedResources.put(Resource.COIN, 3);
        obtainedResources.put(Resource.SERVANT, 2);
        obtainedResources.put(Resource.SHIELD, 1);
        gameController.dropPlayerResources(obtainedResources,resourcesToDrop,
                game1.getCurrentPlayer().getUsername());
        assertEquals(obtainedResources,warehouse.getResources());

        for (Player player:other){
            assertEquals(mapPosition.get(player)+3,player.getFaithTrack().getPosition());
        }
    }

    @Test
    public void movePlayers(){
        Map<Player,Integer> mapPosition=new HashMap<>();

        for (Player player:game1.getPlayers()){
            mapPosition.put(player,player.getFaithTrack().getPosition());
        }
        gameController.movePlayers(game1.getPlayers(),5);

        for (Player player:game1.getPlayers()){
            assertEquals(mapPosition.get(player)+5,player.getFaithTrack().getPosition());
        }
    }

    @Test
    public void dropLeader(){
        gameController.startGame();
        int position=game1.getCurrentPlayer().getFaithTrack().getPosition();
        List<LeaderCard> leaderCards=game1.getCurrentPlayer().getLeaderCards();
        leaderCards.remove(0);
        gameController.dropLeader(0);
        assertEquals(leaderCards,game1.getCurrentPlayer().getLeaderCards());
        assertEquals(position+1,game1.getCurrentPlayer().getFaithTrack().getPosition());
    }

    @Test
    public void switchShelves(){
        gameController.startGame();
        Map<Resource,Integer> resourceIntegerMap;
        Warehouse warehouse=game1.getCurrentPlayer().getPlayerBoard().getWarehouse();
        Warehouse result=new Warehouse();
        for(String shelfName:warehouse.getShelfNames()){
            resourceIntegerMap=new HashMap<>();
            for(Resource resource:warehouse.getShelfResources(shelfName).keySet()){
                if(warehouse.getShelfResources(shelfName).get(resource)>0)
                    resourceIntegerMap.put(resource,warehouse.getShelfResources(shelfName).get(resource));
            }
            result.getShelf(shelfName).addResources(resourceIntegerMap);
        }

        gameController.switchShelves("top","middle");

        result.switchShelves("top","middle");

        assertEquals(result.getResources(),warehouse.getResources());

        exceptionRule.expect(InvalidParameterException.class);
        gameController.switchShelves("bottom","top");
    }

    @Test
    public void computeNextPossibleMoves(){
        gameController.startGame();
        List<String> allMoves=new ArrayList<>(
                Arrays.asList("GET_MARBLES", "ACTIVATE_PRODUCTION", "BUY_CARD", "DROP_LEADER", "PLAY_LEADER", "SWITCH_SHELVES"));

        assertTrue(gameController.computeNextPossibleMoves(true).contains("END_TURN"));
        assertFalse(gameController.computeNextPossibleMoves(false).contains("END_TURN"));


        if(game1.getCurrentPlayer().canPlayLeader(0) ||
            game1.getCurrentPlayer().canPlayLeader(1)){
            assertTrue(gameController.computeNextPossibleMoves(false).contains("PLAY_LEADER"));
        }else {
            assertFalse(gameController.computeNextPossibleMoves(false).contains("PLAY_LEADER"));
        }

        if(game1.getCurrentPlayer().canActivateProduction() ||
            game1.getCurrentPlayer().canActivateBasicProduction()){
            assertTrue(gameController.computeNextPossibleMoves(false).contains("ACTIVATE_PRODUCTION"));
        }else {
            assertFalse(gameController.computeNextPossibleMoves(false).contains("ACTIVATE_PRODUCTION"));
        }

        boolean buyCard_placeCard=false;

        for (MarketCardStack marketCardStack: game1.getMarket().getCardsGrid()){
            for (DevelopmentCard developmentCard: marketCardStack){
                if(game1.getCurrentPlayer().canBuyAndPlaceDevelopmentCard(developmentCard)) {
                    assertTrue(gameController.computeNextPossibleMoves(false).contains("BUY_CARD"));
                    buyCard_placeCard=true;
                }
            }
        }

        if(!buyCard_placeCard) {
            assertFalse(gameController.computeNextPossibleMoves(false).contains("BUY_CARD"));
        }


        Warehouse warehouse=game1.getCurrentPlayer().getPlayerBoard().getWarehouse();
        if( warehouse.canSwitchShelves("top",    "middle") ||
                warehouse.canSwitchShelves("top",    "bottom") ||
                warehouse.canSwitchShelves("top",    "extra1") ||
                warehouse.canSwitchShelves("top",    "extra2") ||
                warehouse.canSwitchShelves("middle", "bottom") ||
                warehouse.canSwitchShelves("middle", "extra1") ||
                warehouse.canSwitchShelves("middle", "extra2") ||
                warehouse.canSwitchShelves("bottom", "extra1") ||
                warehouse.canSwitchShelves("bottom", "extra2")
        ){
            assertTrue(gameController.computeNextPossibleMoves(false).contains("SWITCH_SHELVES"));
        }else {
            assertFalse(gameController.computeNextPossibleMoves(false).contains("SWITCH_SHELVES"));
        }

        game1.getCurrentPlayer().getPlayerBoard().getStrongbox().useResources(game1.getCurrentPlayer().getPlayerBoard().getStrongbox().getResources());
        for(Shelf shelf: game1.getCurrentPlayer().getPlayerBoard().getWarehouse().getShelves()){
            shelf.useResources(shelf.getResources());
        }

        allMoves.remove("ACTIVE_PRODUCTION");
        allMoves.remove("BUY_CARD");
        allMoves.remove("PLAY_LEADER");
        allMoves.remove("SWITCH_SHELVES");
        allMoves.remove("ACTIVATE_PRODUCTION");

        assertEquals(allMoves,gameController.computeNextPossibleMoves(false));
    }

    @Test
    public void getBuyableDevelopmentCards(){
        gameController.startGame();
        List<DevelopmentCard>buyableDevelopmentCards=gameController.getBuyableDevelopmentCards();

        boolean contain;
        for (DevelopmentCard developmentCard:buyableDevelopmentCards){
            contain=false;
            assertTrue(game1.getCurrentPlayer().canBuyAndPlaceDevelopmentCard(developmentCard));
            for (MarketCardStack cardStack:game1.getMarket().getCardsGrid()){
                if (cardStack.peek().equals(developmentCard)) {
                    contain = true;
                    break;
                }
            }
            assertTrue(contain);
        }
    }

    @Test
    public void getPlayableLeaderCards(){
        List<LeaderCard> playableLeaderCards=gameController.getPlayableLeaderCards();
        for (LeaderCard leaderCard:playableLeaderCards){
            assertTrue(game1.getCurrentPlayer().canPlayLeader(game1.getCurrentPlayer().getLeaderCards().indexOf(leaderCard)));
        }
    }

    @Test
    public void activateProduction(){
        gameController.startGame();
        Map<Resource,Integer> res=new HashMap<>(game1.getCurrentPlayer().getPlayerBoard().getResources());
        List<Integer> integerLis=new ArrayList<>();
        integerLis.add(0);
        integerLis.add(1);
        integerLis.add(2);
        int oldPosition=game1.getCurrentPlayer().getFaithTrack().getPosition();
        int faithPoints=0;

        List<DevelopmentCard> buyableDevelopmentCards=gameController.getBuyableDevelopmentCards();
        DevelopmentCard cadToBuy=buyableDevelopmentCards.get(0);

        Set<Resource> resources=cadToBuy.getProductionPower().getInput().keySet();
        for(Resource resource:resources){
            res.put(resource, res.get(resource)-cadToBuy.getProductionPower().getInput().get(resource));
        }

        resources=cadToBuy.getProductionPower().getOutput().keySet();
        for(Resource resource: resources){
            if(resource.equals(Resource.FAITH))
                oldPosition+=cadToBuy.getProductionPower().getOutput().get(resource);
            else
                res.put(resource, res.get(resource)+cadToBuy.getProductionPower().getOutput().get(resource));
        }

        gameController.buyDevelopmentCard(0,cadToBuy);

        for(Resource resource:cadToBuy.getCost().keySet()){
            res.put(resource, res.get(resource)-cadToBuy.getCost().get(resource));
        }

        buyableDevelopmentCards=gameController.getBuyableDevelopmentCards();
        cadToBuy=buyableDevelopmentCards.get(0);

        resources=cadToBuy.getProductionPower().getInput().keySet();
        for(Resource resource:resources){
            res.put(resource, res.get(resource)-cadToBuy.getProductionPower().getInput().get(resource));
        }

        resources=cadToBuy.getProductionPower().getOutput().keySet();
        for(Resource resource: resources){
            if(resource.equals(Resource.FAITH))
                oldPosition+=cadToBuy.getProductionPower().getOutput().get(resource);
            else
                res.put(resource, res.get(resource)+cadToBuy.getProductionPower().getOutput().get(resource));
        }
        gameController.buyDevelopmentCard(1,cadToBuy);


        for(Resource resource:cadToBuy.getCost().keySet()){
            res.put(resource, res.get(resource)-cadToBuy.getCost().get(resource));
        }


        buyableDevelopmentCards=gameController.getBuyableDevelopmentCards();
        cadToBuy=buyableDevelopmentCards.get(0);

        resources=cadToBuy.getProductionPower().getInput().keySet();
        resources.remove(Resource.FAITH);
        for(Resource resource:resources){
            res.put(resource, res.get(resource)-cadToBuy.getProductionPower().getInput().get(resource));
        }

        resources=cadToBuy.getProductionPower().getOutput().keySet();
        for(Resource resource: resources){
            if(resource.equals(Resource.FAITH))
                oldPosition+=cadToBuy.getProductionPower().getOutput().get(resource);
            else
                res.put(resource, res.get(resource)+cadToBuy.getProductionPower().getOutput().get(resource));
        }

        gameController.buyDevelopmentCard(2,cadToBuy);

        for(Resource resource:cadToBuy.getCost().keySet()){
            res.put(resource, res.get(resource)-cadToBuy.getCost().get(resource));
        }

        gameController.activateProduction(integerLis,null,null,null);

        assertEquals(res, game1.getCurrentPlayer().getPlayerBoard().getResources());
        assertEquals(oldPosition+faithPoints,game1.getCurrentPlayer().getFaithTrack().getPosition());


        Map<Resource,Integer> input=new HashMap<>();
        Map<Resource,Integer> output=new HashMap<>();

        input.put(Resource.SHIELD,2);
        output.put(Resource.COIN,1);
        ProductionPower basicProduction=new ProductionPower(input,output);

        gameController.activateProduction(null,basicProduction,null,null);

        res.put(Resource.SHIELD,res.get(Resource.SHIELD)-2);
        res.put(Resource.COIN,res.get(Resource.COIN)+1);

        assertEquals(res,game1.getCurrentPlayer().getPlayerBoard().getResources());

        List<LeaderCard> leaderCards=game1.getCurrentPlayer().getLeaderCards();
        if(leaderCards.get(0).getEffectScope().equals("Production")) {
            gameController.playLeader(0);
            List<Integer> leaderCardIndex = new ArrayList<>();
            leaderCardIndex.add(0);
            List<Resource> resOut = new ArrayList<>();
            resOut.add(Resource.STONE);
            gameController.activateProduction(null, null, leaderCardIndex, resOut);

            res.put(Resource.STONE, res.get(Resource.STONE) + 1);
            res.put(Resource.COIN, res.get(Resource.COIN) - 1);

            assertEquals(res, game1.getCurrentPlayer().getPlayerBoard().getResources());
        }
    }

    @Test
    public void movePlayerTest(){
        int position = game1.getCurrentPlayer().getFaithTrack().getPosition();
        gameController.movePlayer("p1", 1);
        assertEquals(game1.getCurrentPlayer().getFaithTrack().getPosition(), position+1);
    }

    @Test
    public void activatePopeReportTest(){
        assertEquals(game1.getCurrentPlayer().getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.INACTIVE);
        assertEquals(game1.getPlayers().get(1).getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.INACTIVE);
        for (int i = 0; i < 8; i++) {
            gameController.getGame().getPlayers().get(0).getFaithTrack().move();
        }
        assertEquals(game1.getCurrentPlayer().getFaithTrack().inOnPopeSpace(), 1);
        assertTrue(game1.getCurrentPlayer().getFaithTrack().isInPopeFavorByLevel(1));
        assertFalse(game1.getPlayers().get(1).getFaithTrack().isInPopeFavorByLevel(1));
        gameController.activatePopeReport();
        assertEquals(game1.getCurrentPlayer().getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.ACTIVE);
        assertEquals(game1.getPlayers().get(1).getFaithTrack().getPopesFavorTiles().get(0).getState(), PopesFavorTileState.DISCARDED);
    }

    @Test
    public void buyDevelopmentCardTest(){
        DevelopmentCard card =  game1.getMarket().getCardsGrid().get(0).peek();
        gameController.buyDevelopmentCard(1,card);
        assertEquals(game1.getCurrentPlayer().getPlayerBoard().getCardStacks().get(1).peek(), card);
        assertNotEquals(game1.getCurrentPlayer().getPlayerBoard().getCardStacks().get(1).peek(), game1.getMarket().getCardsGrid().get(0).peek());
    }

    @Test
    public void playLeaderTest(){
        gameController.playLeader(0);
        assertEquals(game1.getCurrentPlayer().getPlayedLeaderCards().get(0).getEffectObject(), Resource.STONE);
        assertEquals(game1.getCurrentPlayer().getPlayedLeaderCards().get(0).getEffectScope(), "Storage");
    }

    @Test
    public void computeStandingTest(){
        gameController.movePlayer("p1", 24);
        Map<String, Integer> standing = gameController.computeStanding();
        assertTrue(standing.get("p1") > standing.get("p2"));

    }

    @Test
    public void computeWinnerTest(){
        gameController.movePlayer("p1", 24);
        assertEquals(gameController.computeWinner(), "p1");
    }

    @Test
    public void getStacksToPlaceCardsTest(){
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(11).peek()).size(), 3);
        gameController.buyDevelopmentCard(0, game1.getMarket().getCardsGrid().get(11).peek());
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(6).peek()).size(), 1);
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(10).peek()).size(), 2);
        gameController.buyDevelopmentCard(1, game1.getMarket().getCardsGrid().get(10).peek());
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(9).peek()).size(), 1);
        gameController.buyDevelopmentCard(2, game1.getMarket().getCardsGrid().get(9).peek());
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(11).peek()).size(), 0);
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(0).peek()).size(), 0);
        gameController.buyDevelopmentCard(1, game1.getMarket().getCardsGrid().get(11).peek());
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(6).peek()).size(), 3);
        gameController.buyDevelopmentCard(1, game1.getMarket().getCardsGrid().get(6).peek());
        assertEquals(gameController.getStacksToPlaceCard(game1.getCurrentPlayer(),game1.getMarket().getCardsGrid().get(0).peek()).size(), 1);

    }

}