package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.view.UI;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMessageHandler {
    private final UI ui;
    private final ClassicGameController gameController;
    private Player player;
    private Turn currentTurn;

    public LocalMessageHandler(UI ui, ClassicGameController gameController) {
        this.ui = ui;
        this.gameController = gameController;
    }

    public void startPlaying(){
        gameController.getGame().addPlayer(new Player(ui.getUsername()).getUsername());
        gameController.getGame().setCurrentPlayer(0);
        player=gameController.getGame().getCurrentPlayer();
        ui.selectLeaderCards(player.getLeaderCards());
    }

    public void handle(DropInitialLeaderCardsResponseMessage message){
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), player.getUsername());
        startTurn();
    }

    public void startTurn(){
        currentTurn = new Turn(player.getUsername(), gameController.computeNextPossibleMoves(false));
        ui.setGameState(gameController.getGame());
        ui.displayGameState();
        ui.displayPossibleMoves(currentTurn.getMoves());
    }

    private void nextMoves(boolean alreadyPerformedMoves){
        if(!checkGameOver()){
            currentTurn.setAlreadyPerformedMove(alreadyPerformedMoves);
            currentTurn.setMoves(gameController.computeNextPossibleMoves(currentTurn.hasAlreadyPerformedMove()));
            ui.setGameState(gameController.getGame());
            ui.displayGameState();
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    private boolean checkGameOver(){
        if(gameController.isGameOver()){
            Map<String, Integer> standing = gameController.computeStanding();
            String winner = gameController.computeWinner();
            ui.gameOver(winner, standing);
            return true;
        } else return false;
    }

    public void handle(SelectMoveResponseMessage message){
        if(currentTurn.isValidMove(message.getMove(), player.getUsername())){
            switch (message.getMove()){
                case("GET_MARBLES"):
                    ui.selectMarbles(gameController.getGame().getMarket().getMarbleStructure());
                    break;
                case("DROP_LEADER"):
                    ui.discardLeaderCard(player.getLeaderCards());
                    break;
                case ("PLAY_LEADER"):
                    ui.playLeader(gameController.getPlayableLeaderCards());
                    break;
                case("SWITCH_SHELVES"):
                    ui.switchShelves(player.getPlayerBoard().getWarehouse().getShelfNames());
                    break;
                case("ACTIVATE_PRODUCTION"):
                    ui.activateProduction(player.possibleProductionPowersToActive());
                    break;
                case ("BUY_CARD"):
                    ui.buyDevelopmentCard(gameController.getBuyableDevelopementCards());
                    break;
                case("END_TURN"):
                    endTurn();
                    break;
            }
        } else {
            ui.displayString("Invalid Move\nRetry");
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    private void endTurn(){
        gameController.computeNextPlayer();
        startTurn();
    }

    public void handle(DropLeaderCardResponseMessage message){
        gameController.dropLeader(message.getCard());
        ui.displayString("Your have " +player.getFaithTrack().getPosition() +" faith points");
        nextMoves(currentTurn.hasAlreadyPerformedMove());
    }

    public void handle(PlayLeaderResponseMessage message){
        gameController.playLeader(message.getCardIndex());
        nextMoves(currentTurn.hasAlreadyPerformedMove());
    }

    public void handle(SwitchShelvesResponseMessage message){
        if(gameController.switchShelves(message.getShelf1(), message.getShelf2())){
            nextMoves(false);
        } else {
            ui.displayError("You cannot switch these two shelves\n");
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    public void handle(BuyDevelopmentCardResponseMessage message){
        DevelopmentCard developmentCard = gameController.getBuyableDevelopementCards().get(message.getSelectedCardIndex());
        List<Integer> stacks = gameController.getStacksToPlaceCard(player, developmentCard);
       currentTurn.setBoughtDevelopmentCard(developmentCard);
        ui.selectStackToPlaceCard(stacks);
    }

    public void handle(SelectResourceForWhiteMarbleResponseMessage message) {
        List<Resource> resourcesConverted=message.getResources();
        List<Resource> conversionOptions= currentTurn.getConversionOptions();
        if(!conversionOptions.containsAll(resourcesConverted) && resourcesConverted.size()!= currentTurn.getToConvert()){
            ui.displayError("Invalid conversion, try again!\n");
            return;
        }
//        currentTurn.getConverted().addAll(resourcesConverted);
        askToDropResources();
    }

    private void askToDropResources() {
        System.out.println("merge resources");
        Map<Resource, Integer> allResources = new HashMap<>(currentTurn.getConverted());
        System.out.println("ask to drop");
        ui.dropResources(allResources);
    }

    public void handle(SelectMarblesResponseMessage message){
        Map<String, Map<Resource, Integer>> resources = gameController.getMarbles(message.getRowOrColumn(), message.getIndex());
        System.out.println(resources);

        currentTurn.setConverted(resources.get("converted"));
        currentTurn.setToConvert(resources.get("toConvert").get(Resource.ANY));
        currentTurn.setConversionOptions(new ArrayList<>(resources.get("conversionOptions").keySet()));

        if (resources.get("toConvert").containsKey(Resource.ANY) &&
                resources.get("toConvert").get(Resource.ANY) > 0) {
            System.out.println("ask for conversion help");
//            clientConnection.sendMessage(new SelectResourceForWhiteMarbleRequestMessage(resources.get("toConvert").size(),
//                    resources.get("conversionOptions").get(0), resources.get("conversionOptions").get(1)));
            return;
        }
        if (resources.get("converted").keySet().size() == 0) {
            nextMoves(true);
        } else
            askToDropResources();
    }

    public void handle(SelectStackToPlaceCardResponseMessage message){
        if(player.getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).canPlaceCard(currentTurn.getBoughtDevelopmentCard())){
           gameController.buyDevelopmentCard(message.getSelectedStackIndex(), currentTurn.getBoughtDevelopmentCard());
            currentTurn.setAlreadyPerformedMove(true);
            nextMoves(true);
        }else {
            ui.displayError("Action Could not be completed");
            nextMoves(false);
        }
    }

    public void handle(DropResourcesResponseMessage message){
        Map<Resource, Integer> resourcesConverted =currentTurn.getConverted();
        System.out.println(resourcesConverted);
        // check if the selected resources are valid
        try {
            System.out.println("get reources to drop" + message.getResourcesToDrop());
            System.out.println("resources converted" + resourcesConverted);
            gameController.dropPlayerResources(resourcesConverted, message.getResourcesToDrop(),
                    player.getUsername());
            nextMoves(true);
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            ui.displayString("The selected resources are invalid");
            nextMoves(false);
        }

    }

    public void handle(ActivateProductionResponseMessage message){
        if(message.getSelectedStacks()!=null || message.getBasicProduction() != null || message.getExtraProductionPowers() != null) {
            gameController.activateProduction(message.getSelectedStacks(), message.getBasicProduction(), message.getExtraProductionPowers());
            ui.displayString("Your update strongbox: " + gameController.getGame().getCurrentPlayer().getPlayerBoard().getStrongbox());
            nextMoves(true);
        }
        else {
            ui.displayString("Nothing could be done");
            nextMoves(false);
        }
    }
}
