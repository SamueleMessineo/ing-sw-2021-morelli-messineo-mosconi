package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.UI;
import it.polimi.ingsw.view.gui.GUI;

public class ClientApp {
    public static void main(String[] args) {
        UI ui;
        if (args.length == 1 && args[0].equals("cli")) {
            // playing with CLI
            ui = new CLI();
        } else {
            // playing with GUI
            ui = new GUI();
        }
        ui.run();
    }
}
