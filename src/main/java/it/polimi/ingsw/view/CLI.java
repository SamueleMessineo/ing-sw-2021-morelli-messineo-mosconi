package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.network.setup.Room;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements UI {

    private final Client client;
    private final Scanner input;
    private final PrintStream output;
    private String username;


    public CLI(Client client) {
        this.client = client;
        input = new Scanner(System.in);
        output= new PrintStream(System.out);
    }

    public void run(){}

    public void setup(){
        int selection;
        output.println("Good morning Sir,");
        output.println("how shall I call you?");
        username = input.nextLine();
        output.println("Welcome" + username + ", nice to meet you");
        output.println("Only online game available for now");
        output.println("Do wou want to create a room or join an existing one?");
        output.println("1: create\n2: join");
        selection = input.nextInt();
        while (selection != 1 && selection != 2) {
            output.println("selection not valid");
            output.println("1: create\n2: join");
            selection = input.nextInt();
        }
        if (selection == 1) {
            output.println("How many players is this game for?");
            int playersNum = input.nextInt();

            while (playersNum <= 0 || playersNum > 4) {
                output.println("Invalid number, retry");
                playersNum = input.nextInt();
            }

            input.nextLine();//non ha senso ma senza non funziona

            output.println("Is this a private game? [y/n]");
            boolean privateGame = input.nextLine().toLowerCase().startsWith("y");
            client.sendMessage(new CreateRoomMessage(privateGame, playersNum, username));


        }
        else {
            output.println("Do you have a RoomID or do you want to join a public game");
            output.println("1: RoomId, 2: PublicGame");
            selection = input.nextInt();
            while (selection != 1 && selection != 2) {
                output.println("selection not valid");
                output.println("1: create\n2: join");
                selection = input.nextInt();
            }
            if (selection == 1) {
                output.println("Insert roomId");
                int roomId = input.nextInt();
                client.sendMessage(new JoinPrivateRoomMessage(roomId, username));
            }
            else {
                output.println("Insert desired number of players [0 for random]");
                int playersNumber = input.nextInt();
                while (playersNumber < 0 || playersNumber > 4){
                    output.println("Invalid number");
                    output.println("Insert desired number of players [0 for random]");
                    playersNumber = input.nextInt();
                    }
                client.sendMessage(new JoinPublicRoomMessage(playersNumber, username));
            }

        }

    }

    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {
        output.println("Game details:");
        output.println(players);
        output.println(playersNum);
        output.println(RoomId);

    }
}
