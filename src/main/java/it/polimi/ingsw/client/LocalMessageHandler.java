package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.game.DropInitialLeaderCardsResponseMessage;
import it.polimi.ingsw.view.UI;

public class LocalMessageHandler {
    private final UI ui;
    private final ClassicGameController gameController;
    private Player player;

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
        System.out.println("dropping");
        gameController.dropInitialLeaderCards(message.getCard1(), message.getCard2(), player.getUsername());
        ui.setGameState(gameController.getGame());
        ui.displayGameState();
    }
}
