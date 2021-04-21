package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements UI {

    private Scanner input;
    private PrintStream output;

    public CLI() {
        input = new Scanner(System.in);
        output= new PrintStream(System.out);
    }

    public void run(){}

    public void setup(){

    }

    @Override
    public void displayRoomDettails(ArrayList<String> players, int playersNum, String RoomId) {

        output.println(players);
        output.println(playersNum);
        output.println(RoomId);
    }
}
