package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.client.SelectMoveRequestMessage;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;

import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CLI implements UI {

    private final Client client;
    private final Scanner input;
    private final PrintStream output;
    private String username;
    private Game gameState;
    
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

    private int askIntegerInput(String message, int minBoundary, int maxBoundary) {
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

        client.sendMessage(new DropInitialLeaderCardsResponseMessage(selection1, selection2));

    }

    public void displayLeaderCards(ArrayList<LeaderCard> leaderCards){
        for (int i = 0; i < leaderCards.size(); i++) {
            output.println("Carta numero "+ (i+1) +":" + leaderCards.get(i));
        }
    }

    @Override
    public void displayGameState() {
       displayPlayerBoard(gameState.getPlayerByUsername(username));

        System.out.println(gameState.getCurrentPlayer());
       if (gameState.getCurrentPlayer()!= gameState.getPlayerByUsername(username)) {
          askToDisplayPlayerBoard();
       }
    }

    private void askToDisplayPlayerBoard(){
        output.println("Enter the username of a player you want to visit");
        output.print("[");
        for (Player player:
                gameState.getPlayers()) {
            output.print(player.getUsername() + "(" + player.getVP() + " points) ");

        }
        output.println("]");
        output.print("username: ");
        Player player;
        try {
            player = gameState.getPlayerByUsername(input.nextLine());
            displayPlayerBoard(player);
        } catch (NoSuchElementException e){
            output.println("Username not found");
        }
    }

    private void displayPlayerBoard(Player player){
        output.println(player.getUsername() + " playerBoard: ");
        output.println("\nFaith track positions");
        output.println("position: " + player.getFaithTrack().getPosition());
        output.println("\nStorage");
        output.println(player.getPlayerBoard());
        output.println("\nCards");
        for (int i = 0; i < 3 ; i++) {
            output.println(player.getPlayerBoard().getCardStacks().get(i).toString());
        }
    }

    public void displayPossibleMoves(List<String> moves){
        output.println("Possible moves:");
        output.println("1. Visit a player");
        for (int i = 0; i < moves.size(); i++) {
            output.println((i+2)+". " + moves.get(i).toLowerCase());
        }

        int selection = askIntegerInput("Select a move", 1, moves.size()+1);
        if(selection==1)sendMove("VISIT_PLAYER");
        else sendMove(moves.get(selection-2));
    }

    private void sendMove(String move){
        switch (move){
            case("VISIT_PLAYER"):
                askToDisplayPlayerBoard();
                break;
            case ("ACTIVATE_PRODUCTION"):
                System.out.println("activating production");
                break;
            case ("GET_MARBLES"):
                client.sendMessage(new SelectMoveResponseMessage("GET_MARBLES"));
                break;
            case ("BUY_CARD"):
                System.out.println("buying card");
                break;
            case ("PLAY_LEADER"):
                System.out.println("PLaying leader");
                break;
            case ("DROP_LEADER"):
                client.sendMessage(new SelectMoveResponseMessage("DROP_LEADER"));
                break;
            case ("SWITCH_SHELVES"):
                client.sendMessage(new SelectMoveResponseMessage("SWITCH_SHELVES"));
                break;
            case ("END_TURN"):
                client.sendMessage(new SelectMoveResponseMessage("END_TURN"));
                break;
        }
    }

    public void setGameState(Game game){
        gameState = game;
    }

    public void displayMarbles(MarbleStructure marbleStructure){
        output.println(marbleStructure.toString());

        int selection = askIntegerInput("Do you want to shift a row or a column?\n1.Row\n2.Column", 1,2);

        int selectionIndex;
        if (selection == 1){
            selectionIndex = askIntegerInput("Which row do you want to shift?", 1,3)-1;
            client.sendMessage(new SelectMarblesResponseMessage("ROW",selectionIndex));
        } else {
            selectionIndex = askIntegerInput("Which column do you want to shift?", 1,4)-1;
            client.sendMessage(new SelectMarblesResponseMessage("COLUMN",selectionIndex));
        }
    }

    @Override
    public void dropResources(List<Resource> resources) {
        output.println("This are the resources you just got");
        output.println(resources);
    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {
        displayLeaderCards(cards);
        int selection = askIntegerInput("Select the card number", 1, 2)-1;
        client.sendMessage(new DropLeaderCardResponseMessage(selection));

    }

    @Override
    public void switchShelves(ArrayList<Shelf> shelves) {
        output.println(shelves);
        String selection1;
        String selection2;
        do {
            selection1 = input.nextLine().toLowerCase().trim();
            selection2 = input.nextLine().toLowerCase().trim();
            if(selection1.equals(selection2))output.println("You must select two distinct shelves");
        } while (selection1.equals(selection2));

        client.sendMessage(new SwitchShelvesResponseMessage(selection1, selection2));

    }
}
