package it.polimi.ingsw.network;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ServerConnection;
import it.polimi.ingsw.network.client.*;
import it.polimi.ingsw.network.client.UpdateGameStateMessage;
import it.polimi.ingsw.view.UI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles messages coming from the server.
 */
public class ClientMessageHandler {
    private final ServerConnection serverConnection;
    private final UI ui;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ClientMessage latestMessage = null;

    /**
     * @param client Client to which this handler is connected
     * @param serverConnection client's ServerConnection
     */
    public ClientMessageHandler(Client client, ServerConnection serverConnection){
        this.serverConnection = serverConnection;
        this.ui = serverConnection.getClient().getUi();
    }

    /**
     * Receives room details from the server and shows them in the UI.
     * @param roomDetailsMessage containing game infos.
     */
    public void handle(RoomDetailsMessage roomDetailsMessage){
        executor.submit(() -> ui.displayRoomDetails(roomDetailsMessage.getPlayers(),
                roomDetailsMessage.getPlayersNum(), roomDetailsMessage.getRoomId()));
    }

    /**
     * Receives an error from the server and shows it in the UI.
     * @param message with a String
     */
    public void handle(ErrorMessage message) {
        executor.submit(() -> ui.displayError(message.getBody()));
        if (latestMessage != null ){
            if( !message.getBody().contains("disconnected")){
                latestMessage.accept(this);
            }
        }
        else executor.submit(ui::setup);
     }

    /**
     * Receives a text from the server and shows it in the UI.
     * @param message with a String
     */
    public void handle(StringMessage message) {
        executor.submit(() -> ui.displayString(message.getBody()));
    }

    /**
     * Receives player's leader cards from the client and shows them to the user that decides which to drop.
     * @param message containing player's leader cards
     */
    public void handle(DropInitialLeaderCardsRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.selectLeaderCards(message.getLeaderCards()));
    }

    /**
     * Receives the moves the player can perform and shows the in the UI.
     * @param message with a list of strings that are the moves the player can perform.
     */
    public void handle(SelectMoveRequestMessage message) {
        latestMessage = message;
        executor.submit(() -> ui.displayPossibleMoves(message.getMoves()));
    }

    /**
     * Receives the GameState.
     * @param message with the GameState.
     */
    public void handle(UpdateGameStateMessage message) {
        executor.submit(() -> ui.setGameState(message.getGame()));
    }

    /**
     * Receives the GameState and shows it in the UI.
     * @param message with the GameState.
     */
    public void handle(UpdateAndDisplayGameStateMessage message){
        ui.setGameState(message.getGame());
        ui.displayGameState();
    }

    /**
     * Receive the marble structure from which the user can get the marbles.
     * @param message with the marble structure.
     */
    public void handle(SelectMarblesRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.selectMarbles(message.getMarbleStructure()));
    }

    /**
     * Receives a list with the resources the player just got and shows it to the user that selects the
     * ones to  drop.
     * @param message with player new resources.
     */
    public void handle(DropResourceRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.dropResources(message.getResources()));
    }

    /**
     * Receives player's leader cards from the client and shows them to the user that decides which to drop.
     * @param message containing player's leader cards
     */
    public void handle(DropLeaderCardRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.discardLeaderCard(message.getLeaderCards()));
    }

    /**
     * Receives the shelves the player can switch and shows them to the user.
     * @param message with the shelves that the player can switch.
     */
    public void handle(SwitchShelvesRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.switchShelves(message.getShelves()));
    }

    /**
     * Receives the possible ways of converting a white marble.
     * @param message with the possible ways to convert a white marble.
     */
    public void handle(SelectResourceForWhiteMarbleRequestMessage message) {
        latestMessage = message;
        executor.submit(() -> ui.selectResourceForWhiteMarbles(message.getNumberOfWhiteMarbles(), message.getOptions()));
    }

    /**
     * Receives the stacks on which the user can activate the production and shows them in the UI.
     * @param message possible productions to activate.
     */
    public void handle(ActivateProductionRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.activateProduction(message.getProductionPowers()));
    }

    /**
     * Receives the cards the player can buy and shows them in the UI.
     * @param message With a list of cards that the player can buy.
     */
    public void handle(BuyDevelopmentCardRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.buyDevelopmentCard(message.getDevelopmentCards()));
    }

    /**
     * Receives the stacks on which the player can place the new bought cards an shows them in the UI.
     * @param message with a list of the indexes of the stacks where the card can be placed.
     */
    public void handle(SelectStackToPlaceCardRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.selectStackToPlaceCard(message.getStacks()));
    }

    /**
     * Receives the leader cards that the player can play and shows them in the UI.
     * @param message with the indexes of the leader cards the player can play.
     */
    public void handle(PlayLeaderRequestMessage message){
        latestMessage = message;
        executor.submit(() -> ui.playLeader(message.getLeaderCards()));
    }

    /**
     * Shows the player which type of resources and how many of them he can get to start the game.
     * @param message containing the resources and the amount that can be selected.
     */
    public void handle(SelectInitialResourceRequestMessage message) {
        latestMessage = message;
        executor.submit(() -> ui.selectInitialResources(message.getResources(), message.getAmount()));
    }

    /**
     * Receives the winner and the standing and shows the in the UI.
     * @param message with the standing an the  winner.
     */
    public void handle(GameOverMessage message){
        executor.submit(() -> ui.gameOver(message.getWinner(), message.getStanding()));
    }
}
