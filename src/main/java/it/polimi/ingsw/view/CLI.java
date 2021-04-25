package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.network.game.DropInitialLeaderCardsMessage;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;

import java.io.PrintStream;
import java.security.InvalidParameterException;
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

    public void setup(){
        output.println("Good morning Sir,");
        output.println("how shall I call you?");
        username = input.nextLine();
        output.println("Welcome " + username + ", nice to meet you");
        output.println("Only online game available for now");

        output.println("Do wou want to create a room or join an existing one?");
        int selection = askIntegerInput("1: create\n2: join", 1, 2);

        if (selection == 1) {
            int playersNum = askIntegerInput("How many players is this game for?",1,4);

            boolean privateGame;
            if(playersNum==1){
                privateGame = true;
            } else {
                output.println("Is this a private game? [y/n]");
                privateGame= input.nextLine().toLowerCase().startsWith("y");
            }

            client.sendMessage(new CreateRoomMessage(privateGame, playersNum, username));
        }
        else {
            output.println("Do you have a RoomID or do you want to join a public game");
            selection = askIntegerInput("1: RoomId, 2: PublicGame", 1, 2);

            if (selection == 1) {
                int roomId = askIntegerInput("Insert roomId", 1000, 9999);
                client.sendMessage(new JoinPrivateRoomMessage(roomId, username));
            }
            else {
                int playersNumber = askIntegerInput("Insert desired number of players [0 for random]", 0, 4);
                client.sendMessage(new JoinPublicRoomMessage(playersNumber, username));
            }
        }
    }

    @Override
    public void displayError(String body) {
        System.out.println("ERROR!");
        System.out.println(body);

        System.out.println("press enter to retry...");
        input.nextLine();
        setup();
    }

    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {
        output.println("Game details:");
        output.println(players);
        output.println(playersNum);
        output.println(RoomId);

    }

    public int askIntegerInput(String message, int minBoundary, int maxBoundary) {
        int selection;
        while (true) {
            output.println(message);
            try {
                selection = Integer.parseInt(input.nextLine());
                if (selection < minBoundary || selection > maxBoundary) {
                    throw new InvalidParameterException();
                } else {
                    break;
                }
            } catch (NumberFormatException | InvalidParameterException e) {
                output.println("selection not valid");
            }
        }
        return selection;
    }

    public void displayString(String body){
        output.println(body);
    }

    @Override
    public void selectLeaderCards(ArrayList<LeaderCard> leaderCards) {
        displayLeaderCards(leaderCards);
        int selection1;
        int selection2;
        do {
            selection1 = askIntegerInput("select the first card to drop", 1,4)-1;
            selection2 = askIntegerInput("select the second card to drop", 1,4)-1;
            if(selection1==selection2)output.println("You must select two distinct cards");
        } while (selection1 == selection2);

        client.sendMessage(new DropInitialLeaderCardsMessage(selection1, selection2));

    }

    public void displayLeaderCards(ArrayList<LeaderCard> leaderCards){
        for (int i = 0; i < leaderCards.size(); i++) {
            output.println("Carta numero "+ (i+1) +":" + leaderCards.get(i));
        }
    }

}
