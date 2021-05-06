package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.Turn;
import it.polimi.ingsw.model.market.MarketCardStack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.GameUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameMessageHandler {

    private final GameController gameController;
    private final ClientConnection clientConnection;
    private final Room room;

    public GameMessageHandler(GameController gameController, ClientConnection clientConnection, Room room) {
        this.gameController = gameController;
        this.clientConnection = clientConnection;
        this.room = room;

        // calculate the player's position in the playing order
        int playingIndex = room.getConnections().indexOf(clientConnection) - room.getGame().getInkwellPlayer();
        if (playingIndex < 0) {
            playingIndex += room.getNumberOfPlayers();
        }

        if (playingIndex == 2 || playingIndex == 3) {
            // give 1 faith point if the player is the third or fourth to play
            gameController.movePlayer(room.getPlayerFromConnection(clientConnection).getUsername(), 1);
        }

        List<Resource> resourceOptions = new ArrayList<>(Arrays.asList(
                Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD));

        if (playingIndex == 0) {
            clientConnection.sendMessage(
                    new DropInitialLeaderCardsRequestMessage(
                            room.getPlayerFromConnection(clientConnection).getLeaderCards())
            );
        } else {
            // ask players to choose the type of resources they want to receive
            int resourceAmount = (playingIndex == 1 || playingIndex == 2) ? 1 : 2;
            Message message = new SelectInitialResourceRequestMessage(
                    resourceOptions, resourceAmount);
            clientConnection.sendMessage(message);
        }
    }

    public void handle(SelectCardMessage message) {
        System.out.println(message.getNum());
    }

    public void handle(EndTurnMessage message) {
        System.out.println(message.getName());
    }


    public void handle(DropInitialLeaderCardsResponseMessage message){
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), room.getPlayerFromConnection(clientConnection).getUsername());
        GameUtils.saveGameState(room.getGame(), room.getId());
        startPlayingIfReady();
    }

    private void startPlayingIfReady() {
        for (Player p : room.getGame().getPlayers()) {
            if (p.getLeaderCards().size() != 2) return;
        }

       sendStateAndMovesForNextTurn();
    }

    public void handle(SelectMoveResponseMessage message){
        if(room.getCurrentTurn().isValidMove(message.getMove(), room.getPlayerFromConnection(clientConnection).getUsername())){
            switch (message.getMove()){
                case("GET_MARBLES"):
                    clientConnection.sendMessage(new SelectMarblesRequestMessage(room.getGame().getMarket().getMarbleStructure()));
                    break;
                case("DROP_LEADER"):
                    clientConnection.sendMessage(new DropLeaderCardRequestMessage(room.getPlayerFromConnection(clientConnection).getLeaderCards()));
                    break;
                case ("PLAY_LEADER"):
                    clientConnection.sendMessage(new PlayLeaderRequestMessage(gameController.getPlayableLeaderCards()));
                case("SWITCH_SHELVES"):
                    clientConnection.sendMessage(new SwitchShelvesRequestMessage(room.getPlayerFromConnection(clientConnection).getPlayerBoard().getWarehouse().getShelves()));
                    break;
                case("END_TURN"):
                    endTurn();
                    break;
                case("ACTIVATE_PRODUCTION"):
                    clientConnection.sendMessage(new ActivateProductionRequestMessage(room.getPlayerFromConnection(clientConnection).possibleProductionPowersToActive()));
                    break;
                case ("BUY_CARD"):
                    clientConnection.sendMessage(new BuyDevelopmentCardRequestMessage(gameController.getBuyableDevelopementCards()));
            }

        } else {
            clientConnection.sendMessage(new ErrorMessage("Invalid Move\nRetry"));
            clientConnection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
        }
    }

    public void handle(SelectMarblesResponseMessage message){
        System.out.println("received get marbles response");
        Map<String, List<Resource>> resources = gameController.getMarbles(message.getRowOrColumn(), message.getIndex());
        System.out.println("retrieved resources");

        if (gameController.isGameOver()) {
            room.sendAll(new StringMessage("GAME OVER"));
            return;
        }

        room.getCurrentTurn().setConverted(resources.get("converted"));
        room.getCurrentTurn().setToConvert(resources.get("toConvert"));
        room.getCurrentTurn().setConversionOptions(resources.get("conversionOptions"));
        System.out.println("turn set");

        if (resources.get("toConvert").size() > 0 && resources.get("toConvert").get(0).equals(Resource.ANY)) {
            System.out.println("ask for conversion help");
            clientConnection.sendMessage(new SelectResourceForWhiteMarbleRequestMessage(resources.get("toConvert").size(),
                    resources.get("conversionOptions").get(0), resources.get("conversionOptions").get(1)));
            return;
        }

        askToDropResources();
    }

    private void askToDropResources() {
        System.out.println("merge resources");
        List<Resource> allResources = new ArrayList<>();

        allResources.addAll(room.getCurrentTurn().getConverted());

        System.out.println("ask to drop");

        clientConnection.sendMessage(new DropResourceRequestMessage(allResources));
    }

    public void handle(DropLeaderCardResponseMessage message){
        gameController.dropLeader(message.getCard());
        clientConnection.sendMessage(new StringMessage("Your have " +room.getPlayerFromConnection(clientConnection).getFaithTrack().getPosition() +" faith points"));
        sendNextMoves(false);
    }

    public void handle(PlayLeaderResponseMessage message){

    }

    public void handle(SwitchShelvesResponseMessage message){
        if(gameController.switchShelves(message.getShelf1(), message.getShelf2())){
            clientConnection.sendMessage(new StringMessage("Your updated warehouse\n" + room.getPlayerFromConnection(clientConnection).getPlayerBoard().getWarehouse().getShelf("top") +
                    room.getPlayerFromConnection(clientConnection).getPlayerBoard().getWarehouse().getShelf("middle") + room.getPlayerFromConnection(clientConnection).getPlayerBoard().getWarehouse().getShelf("bottom") + room.getPlayerFromConnection(clientConnection).getPlayerBoard().getWarehouse().getShelf("extra")));
                    sendNextMoves(false);
        } else {
            clientConnection.sendMessage(new ErrorMessage("You cannot switch these two shelves\n"));
            clientConnection.sendMessage((new SelectMoveRequestMessage(room.getCurrentTurn().getMoves())));
        }
    }

    public void handle(SelectResourceForWhiteMarbleResponseMessage message) {
        List<Resource> resourcesConverted=message.getResources();
        List<Resource> conversionOptions= room.getCurrentTurn().getConversionOptions();
        if(!conversionOptions.containsAll(resourcesConverted) && resourcesConverted.size()!= room.getCurrentTurn().getToConvert().size()){
            clientConnection.sendMessage(new ErrorMessage("Invalid conversion, try again!\n"));
            return;
        }
        room.getCurrentTurn().getConverted().addAll(resourcesConverted);
        askToDropResources();
    }

    public void handle(ActivateProductionResponseMessage message){
        if(message.getSelectedStacks()!=null){
            room.getGame().getCurrentPlayer().getPlayerBoard().activateProduction(message.getSelectedStacks());
            clientConnection.sendMessage(new StringMessage("Your update strongbox: "+ room.getGame().getCurrentPlayer().getPlayerBoard().getStrongbox()));
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves(room.getCurrentTurn().hasAlreadyPerformedMove());
        }
        if(message.getBasicProduction() != null){
            //activating basic production
        }
        else {
            clientConnection.sendMessage(new ErrorMessage("Nothing could be done"));
            sendNextMoves(false);
        }
    }

    public void handle(DropResourcesResponseMessage message){
        List<Resource> resourcesConverted= room.getCurrentTurn().getConverted();
        if(message.getResourcesToDrop()==null && !resourcesConverted.containsAll(message.getResourcesToDrop())){
            clientConnection.sendMessage(new ErrorMessage("Nothing could be done"));
            return;
        }
        //TODO: to be completed
    }

    public void handle(BuyDevelopmentCardResponseMessage message){
        DevelopmentCard developmentCard = gameController.getBuyableDevelopementCards().get(message.getSelectedCardIndex());
        List<DevelopmentCard> stacks = new ArrayList<>();
        for (PlayerCardStack cardStack:
        room.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks()){
            if(cardStack.canPlaceCard(developmentCard))stacks.add(cardStack.peek());
        }
        clientConnection.sendMessage(new SelectStackToPlaceCardRequestMessage(stacks));

        room.getCurrentTurn().setBuyedDevelopmentCard(developmentCard);


    }

    public void handle(SelectStackToPlaceCardResponseMessage message){
        if(room.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).canPlaceCard(room.getCurrentTurn().getBuyedDevelopmentCard())){
            room.getGame().getCurrentPlayer().getPlayerBoard().getCardStacks().get(message.getSelectedStackIndex()).add(room.getCurrentTurn().getBuyedDevelopmentCard());
            room.getGame().getCurrentPlayer().getPlayerBoard().payResourceCost(room.getGame().getCurrentPlayer().computeDiscountedCost(room.getCurrentTurn().getBuyedDevelopmentCard()));
            room.getCurrentTurn().setAlreadyPerformedMove(true);
            sendNextMoves(room.getCurrentTurn().hasAlreadyPerformedMove());

            if (gameController.isGameOver()) {
                room.sendAll(new StringMessage("GAME OVER"));
            }

        }else {
            clientConnection.sendMessage(new ErrorMessage("Action Could not be completed"));
            sendNextMoves(room.getCurrentTurn().hasAlreadyPerformedMove());
        }

    }

    private void endTurn(){
        gameController.computeCurrentPlayer();
        sendStateAndMovesForNextTurn();

    }

    private void sendStateAndMovesForNextTurn(){

        ClientConnection currentPlayer = room.getConnections().get(room.getGame().getPlayers().indexOf(
                room.getGame().getCurrentPlayer()));
        room.sendAll(new GameStateMessage(room.getGame()));

        if(!gameController.isGameOver() || (room.getGame().getPlayers().indexOf(room.getPlayerFromConnection(currentPlayer)) != room.getGame().getInkwellPlayer())){
            room.setCurrentTurn(new Turn(room.getPlayerFromConnection(currentPlayer).getUsername(), gameController.computeNextPossibleMoves(false)));
            SelectMoveRequestMessage selectMoveRequestMessage = new SelectMoveRequestMessage(room.getCurrentTurn().getMoves());
            currentPlayer.sendMessage(selectMoveRequestMessage);
        } else {
            Map<String, Integer> standing = gameController.computeStanding();
            String winner = gameController.computeWinner();
            clientConnection.sendMessage(new GameOverMessage(winner, standing));
        }
    }

    private void sendNextMoves(boolean hasPerformedAction){
        room.getCurrentTurn().setMoves(gameController.computeNextPossibleMoves(hasPerformedAction));
        clientConnection.sendMessage(new SelectMoveRequestMessage(room.getCurrentTurn().getMoves()));
    }

    public void handle(SelectInitialResourceResponseMessage message) {
        gameController.giveInitialResources(message.getSelectedResources(),
                room.getPlayerFromConnection(clientConnection).getUsername());
        clientConnection.sendMessage(new DropInitialLeaderCardsRequestMessage(
                room.getPlayerFromConnection(clientConnection).getLeaderCards()));
    }
}
