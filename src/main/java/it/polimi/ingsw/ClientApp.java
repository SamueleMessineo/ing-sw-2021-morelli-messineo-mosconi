package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LocalClient;
import it.polimi.ingsw.view.SelectionCLI;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        Client client;
        SelectionCLI cli = new SelectionCLI();

        if (args.length == 1 && args[0].equals("cli")) {
            if(cli.playingOnLine()){
                client = new Client(true);
            } else {
                client = new LocalClient(true);
            }
        } else if (args.length == 0) {
            client = new Client(false);
        } else {
            System.out.println("error");
            return;
        }

        try {
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
