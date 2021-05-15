package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.SoloGameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.view.UI;

public class LocalClient extends Client {
    private final LocalMessageHandler localMessageHandler;
    private final UI ui;
    public LocalClient(UI ui) {
        super(ui);
        this.ui = super.getUi();
        ClassicGameController gameController = new SoloGameController(new Game());
        localMessageHandler = new LocalMessageHandler(ui, gameController);
    }

    @Override
    public void run() {
        ui.askUsername();
        localMessageHandler.startPlaying();
    }

    @Override
    public void sendMessage(Message m) {
        GameMessage gameMessage = (GameMessage)m;
        gameMessage.accept(localMessageHandler);
    }
}
