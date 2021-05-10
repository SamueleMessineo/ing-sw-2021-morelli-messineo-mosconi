package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.LocalGameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.view.UI;

import java.io.IOException;

public class LocalClient extends Client {
    private final UI ui;
    private final LocalMessageHandler localMessageHandler;
    public LocalClient(boolean withCLI) {
        super(withCLI);
        ui = super.getUi();
        ClassicGameController gameController = new LocalGameController(new Game());
        localMessageHandler = new LocalMessageHandler(ui,gameController);
    }

    @Override
    public void run() throws IOException {
        ui.askUsername();
        localMessageHandler.startPlaying();
    }

    @Override
    public void sendMessage(Message m) {
        System.out.println("handling");
        GameMessage gameMessage = (GameMessage)m;
        gameMessage.accept(localMessageHandler);
    }
}
