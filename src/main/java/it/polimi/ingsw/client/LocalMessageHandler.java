package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.view.UI;

import java.util.ArrayList;
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

    private void nextMoves(){
        if(!checkGameOver()){
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
                    ui.playLeader(player.getLeaderCards());
                case("SWITCH_SHELVES"):
                    ui.switchShelves(player.getPlayerBoard().getWarehouse().getShelfNames());
                    break;
                case("ACTIVATE_PRODUCTION"):
                    ui.activateProduction(player.possibleProductionPowersToActive());
                    break;
                case ("BUY_CARD"):
                    ui.buyDevelopmentCard(gameController.getBuyableDevelopementCards());
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
        nextMoves();
    }

    public void handle(PlayLeaderResponseMessage message){
        gameController.playLeader(message.getCardIndex());
        nextMoves();
    }

    public void handle(SwitchShelvesResponseMessage message){
        if(gameController.switchShelves(message.getShelf1(), message.getShelf2())){
            nextMoves();
        } else {
            ui.displayError("You cannot switch these two shelves\n");
            ui.displayPossibleMoves(currentTurn.getMoves());
        }
    }

    public void handle(BuyDevelopmentCardResponseMessage message){
        DevelopmentCard developmentCard = gameController.getBuyableDevelopementCards().get(message.getSelectedCardIndex());
        List<DevelopmentCard> stacks = new ArrayList<>();
        for (PlayerCardStack cardStack:
                player.getPlayerBoard().getCardStacks()){
            if(cardStack.canPlaceCard(developmentCard))stacks.add(cardStack.peek());
        }
        ui.selectStackToPlaceCard(stacks);
        currentTurn.setBuyedDevelopmentCard(developmentCard);
    }

    public void handle(SelectStackToPlaceCardResponseMessage message){
        if(player.getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).canPlaceCard(currentTurn.getBuyedDevelopmentCard())){
            player.getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).add(currentTurn.getBuyedDevelopmentCard());
            player.getPlayerBoard().payResourceCost(player.computeDiscountedCost(currentTurn.getBuyedDevelopmentCard()));
            currentTurn.setAlreadyPerformedMove(true);
            nextMoves();

        }else {
            ui.displayError("Action Could not be completed");
            nextMoves();
        }

    }
}
