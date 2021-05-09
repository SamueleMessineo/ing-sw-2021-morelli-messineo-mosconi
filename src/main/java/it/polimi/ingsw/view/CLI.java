package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Shelf;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;

import java.io.PrintStream;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CLI implements UI {

    private final Client client;
    private final Scanner input;
    private final PrintStream output;
    private String username;
    private Game gameState;

    private ExecutorService executor = Executors.newCachedThreadPool();

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
    public void selectInitialResources(List<Resource> resources, int amount) {
        output.println("Select initial resource(s)");
        List<Resource> selectedResources = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            for (int resourceIndex = 1; resourceIndex <= resources.size(); resourceIndex++) {
                output.println(resourceIndex + ": " + resources.get(resourceIndex-1));
            }
            int selection = askIntegerInput("select resource", 1, resources.size());
            selectedResources.add(resources.get(selection-1));
        }

        client.sendMessage(new SelectInitialResourceResponseMessage(selectedResources));
    }

    @Override
    public void selectLeaderCards(ArrayList<LeaderCard> leaderCards) {
        displayLeaderCards(leaderCards);
        int selection1;
        int selection2;
        do {
            selection1 = askIntegerInput("Select the first card to drop", 1,4)-1;
            selection2 = askIntegerInput("Select the second card to drop", 1,4)-1;
            if(selection1==selection2)output.println("You must select two distinct cards");
        } while (selection1 == selection2);

        client.sendMessage(new DropInitialLeaderCardsResponseMessage(selection1, selection2));
        output.println("Waiting for other players to select their cards");

    }

    public void displayLeaderCards(List<LeaderCard> leaderCards){
        for (int i = 0; i < leaderCards.size(); i++) {
            output.println("Card number "+ (i+1) +":" + leaderCards.get(i));
        }
    }

    @Override
    public void displayGameState() {
        // displayPlayerBoard(gameState.getPlayerByUsername(username));

        displayGameBoard();
       //momentary fix

        if(gameState.getCurrentPlayer()!= gameState.getPlayerByUsername(username)){
           for (Player player:
                gameState.getPlayers()) {
                   displayPlayerBoard(gameState.getPlayerByUsername(player.getUsername()));
                   output.println("Wait your turn");
           }
       } else displayPlayerBoard(gameState.getPlayerByUsername(username));

       // todo this in preferable to the above one but gives problems. Go Fix it
        /*
       if (gameState.getCurrentPlayer()!= gameState.getPlayerByUsername(username)) {
           executor.submit(this::askToDisplayPlayerBoard);
       }
         */
    }

    private void displayGameBoard(){
        output.println("\n" + gameState.getMarket().getMarbleStructure());
        System.out.println("\nCards Market:");


        for (int i = 11; i >= 0; i-=3) {
            output.println(gameState.getMarket().getCardsGrid().get(i));
            output.println("\n");
        }
        for (int i = 10; i >= 0; i-=3) {
            output.println(gameState.getMarket().getCardsGrid().get(i));
            output.println("\n");
        }
        for (int i = 9; i >= 0; i-=3) {
            output.println(gameState.getMarket().getCardsGrid().get(i));
            output.println("\n");
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
            } catch (NoSuchElementException e) {
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
        if(selection==1){
            sendMove("VISIT_PLAYER");
            displayPossibleMoves(moves);
        }
        else sendMove(moves.get(selection-2));
    }

    private void sendMove(String move){
        switch (move){
            case("VISIT_PLAYER"):
                askToDisplayPlayerBoard();
                break;
            case ("ACTIVATE_PRODUCTION"):
                client.sendMessage(new SelectMoveResponseMessage(move));
                break;
            case ("GET_MARBLES"):
                client.sendMessage(new SelectMoveResponseMessage(move));
                break;
            case ("BUY_CARD"):
                client.sendMessage(new SelectMoveResponseMessage(move));
                break;
            case ("PLAY_LEADER"):
                client.sendMessage(new SelectMoveResponseMessage(move));
                break;
            case ("DROP_LEADER"):
                client.sendMessage(new SelectMoveResponseMessage(move));
                break;
            case ("SWITCH_SHELVES"):
                client.sendMessage(new SelectMoveResponseMessage(move));
                break;
            case ("END_TURN"):
                client.sendMessage(new SelectMoveResponseMessage(move));
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
        int selection = askIntegerInput("Select the card number", 1, cards.size())-1;
        client.sendMessage(new DropLeaderCardResponseMessage(selection));
    }

    private void displayShelves(List<String> shelvesNames){
        Warehouse warehouse=gameState.getCurrentPlayer().getPlayerBoard().getWarehouse();
        output.println("\nWAREHOUSE:");
        for(String name: shelvesNames){
            output.println(name+": "+warehouse.getShelf(name));
        }
    }

    @Override
    public void switchShelves(ArrayList<String> shelves) {
        displayShelves(shelves);
        String selection1;
        String selection2;
        output.println("Select the name of the shelves you want to switch");
        do {
            selection1 = input.nextLine().toLowerCase().trim();
            selection2 = input.nextLine().toLowerCase().trim();
            if(selection1.equals(selection2))output.println("You must select two distinct shelves");
        } while (selection1.equals(selection2));

        client.sendMessage(new SwitchShelvesResponseMessage(selection1, selection2));

    }

    @Override
    public void activateProduction(List<ProductionPower> productionPowers) {
        List<Integer> selectedStacks = new ArrayList<>();
        ProductionPower selectedBasicProductionPowers = null;
        Game currentGameState = gameState;
        Integer selection;
        boolean done = false;
        List<Integer> indexes= new ArrayList<>();
        List<Integer> extraProductionPowers = new ArrayList<>();

        if(productionPowers.equals(gameState.getCurrentPlayer().possibleProductionPowersToActive())){
            if(currentGameState.getCurrentPlayer().canActivateBasicProduction()){
                indexes.add(0);
            }
            for (int i = 1; i <= productionPowers.size(); i++) {
                indexes.add(i);
            }

            int productionNumber=1;
            while (!done){
                output.println(gameState.getCurrentPlayer().possibleProductionPowersToActive());
                String message;
                message ="Select production power N° " +productionNumber+" to activate" ;
                if(indexes.contains(0) && gameState.getCurrentPlayer().canActivateBasicProduction()){
                    message+="[0 is basic production]";
                }
                message+="\n"+ indexes;

                selection = askIntegerInput(message,indexes.get(0),gameState.getCurrentPlayer().possibleProductionPowersToActive().size());

                if(indexes.contains(selection)) {

                    if (selection == 0) {
                        if (currentGameState.getCurrentPlayer().canActivateBasicProduction()) {
                            selectedBasicProductionPowers = askBasicProductionPowerIO();
                            currentGameState.getCurrentPlayer().getPlayerBoard().payResourceCost(selectedBasicProductionPowers.getInput());
                        }
                    } else {
                            currentGameState.getCurrentPlayer().getPlayerBoard().payResourceCost(currentGameState.getCurrentPlayer().possibleProductionPowersToActive().get(selection - 1).getInput());
                            if (gameState.getCurrentPlayer().getPlayerBoard().getExtraProductionPowers().contains(currentGameState.getCurrentPlayer().possibleProductionPowersToActive().get(selection - 1))){
                                extraProductionPowers.add(selection - (gameState.getCurrentPlayer().possibleProductionPowersToActive().size()-1));
                            } else {
                                selectedStacks.add(selection - 1);
                            }

                    }
                    indexes.remove((Integer) selection);
                    productionNumber++;
                } else output.println("Selection not valid");


                output.println("Are you done? [y/n]");
                done = input.nextLine().trim().toLowerCase().startsWith("y");
            }


            client.sendMessage(new ActivateProductionResponseMessage(selectedStacks, selectedBasicProductionPowers, extraProductionPowers));
        } else output.println("problem with gameState");


        /*
            if(gameState.getCurrentPlayer().canActivateBasicProduction()){
            output.println("Do you want to activate basic production power? [y/n]");
            if (input.nextLine().toLowerCase().trim().startsWith("y")){
                selectedProductionPowers= askBasicProductionPowerIO();
            }
        }

        if(gameState.getCurrentPlayer().canActivateProduction()){
            selectedStacks = new ArrayList<>();
            output.println("Do you want to activate production on the first stack? [y/n]");
            if(input.nextLine().trim().toLowerCase().startsWith("y"))selectedStacks.add(1);
            output.println("Do you want to activate production on the second stack? [y/n]");
            if(input.nextLine().trim().toLowerCase().startsWith("y"))selectedStacks.add(2);
            output.println("Do you want to activate production on the third stack? [y/n]");
            if(input.nextLine().trim().toLowerCase().startsWith("y"))selectedStacks.add(3);
        }


        client.sendMessage(new ActivateProductionResponseMessage(selectedStacks, selectedProductionPowers));
        */

    }

    private ProductionPower askBasicProductionPowerIO(){
        Map<Resource, Integer> input = new HashMap<>();
        Map<Resource, Integer> output = new HashMap<>();
        List<Integer> resources = new ArrayList<>();
        resources.add(askIntegerInput("What is the first resource you want to put as input?\n1.SHIELD, 2.SERVANT, 3.STONE, 4.COIN",1,4));
        resources.add(askIntegerInput("What is the second resource you want to put as input?\n1.SHIELD, 2.SERVANT, 3.STONE, 4.COIN",1,4));
        resources.add(askIntegerInput("What is the resource you want as output?\n1.SHIELD, 2.SERVANT, 3.STONE, 4.COIN",1,4));

        for (int i = 0; i < 3; i++) {
            switch (resources.get(i)){
                case (1):
                    if(i<2)input.put(Resource.SHIELD, 1);
                    else output.put(Resource.SHIELD,1);
                    break;
                case (2):
                    if(i<2)input.put(Resource.SERVANT, 1);
                    else output.put(Resource.SERVANT,1);
                    break;
                case (3):
                    if(i<2)input.put(Resource.STONE, 1);
                    else output.put(Resource.STONE,1);
                    break;
                case (4):
                    if(i<2)input.put(Resource.COIN, 1);
                    else output.put(Resource.COIN,1);
                    break;
            }
        }
        return new ProductionPower(input, output);
    }

    @Override
    public void buyDevelopmentCard(List<DevelopmentCard> developmentCards) {
        output.println(developmentCards);
        int selection = askIntegerInput("Select a card", 1, developmentCards.size())-1;
        client.sendMessage(new BuyDevelopmentCardResponseMessage(selection));

    }

    @Override
    public void selectStackToPlaceCard(List<DevelopmentCard> stacks) {
        output.println(stacks);
        int selection = askIntegerInput("On which stack you want to put your new card?", 1, stacks.size());
        // TODO
    }

    @Override
    public void playLeader(List<LeaderCard> leaderCards) {
        displayLeaderCards(leaderCards);
        int selection = askIntegerInput("Select the card number", 1, leaderCards.size())-1;
        client.sendMessage(new PlayLeaderResponseMessage(selection));
    }

    public void gameOver(String winner, Map<String, Integer> standing){
        output.println("Game ended, "+winner+" won the game\n"+standing);
        int selection = askIntegerInput("Do you want to start a new game?", 1,2);
        if(selection==1)setup();
        else {
            output.println("Bye "+username);
            client.closeConncetion();
        }
    }

    @Override
    public void run() {
        setup();
    }
}
