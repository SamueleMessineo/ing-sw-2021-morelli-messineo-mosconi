package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.client2;

import java.io.IOException;

public class Client2App {
    public static void main(String[] args) {

        client2 client2 = new client2();

        try {
            client2.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}