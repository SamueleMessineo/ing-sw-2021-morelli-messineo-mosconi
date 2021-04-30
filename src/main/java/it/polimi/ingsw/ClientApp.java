package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        Client client;

        if (args.length == 1 && args[0].equals("cli")) {
            client = new Client(true);
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
