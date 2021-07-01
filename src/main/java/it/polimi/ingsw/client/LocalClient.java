package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ClassicGameController;
import it.polimi.ingsw.controller.SoloGameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.network.game.GameMessage;
import it.polimi.ingsw.view.UI;

/**
 * The Client class, it calls the localMessageHandler.
 */
public class LocalClient extends Client {
    private final LocalMessageHandler localMessageHandler;
    private final UI ui;

    public LocalClient(UI ui) {
        super(ui);
        this.ui = super.getUi();
        ClassicGameController gameController = new SoloGameController(new Game());
        localMessageHandler = new LocalMessageHandler(ui, gameController);
    }

    /**
     * If the  username is different from lorenzoIlMagnifico signals to the localMessageHandler to start playing
     */
    @Override
    public void run() {
        do{
            if(ui.getUsername() != null && ui.getUsername().trim().equalsIgnoreCase("lorenzoilmagnifico")){
                ui.displayError("There is only one 'Lorenzo Il Magnifico'");
            }
            ui.askUsername();
        } while (ui.getUsername().trim().equalsIgnoreCase("lorenzoilmagnifico"));

        localMessageHandler.startPlaying();
    }

    /**
     * Calls the accept method of a new message on the localMessageHandler.
     * @param m the message to send.
     */
    @Override
    public void sendMessage(Message m) {
        GameMessage gameMessage = (GameMessage)m;
        gameMessage.accept(localMessageHandler);
    }
}
