package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.utils.GameUtils;

import java.io.PrintStream;
import java.util.*;

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
        askUsername();
        output.println("Do wou want to create a room or join an existing one?");
        int selection = GameUtils.askIntegerInput("1: create\n2: join", 1, 2, output, input);

        if (selection == 1) {
            int playersNum = GameUtils.askIntegerInput("How many players is this game for?",1,4, output, input);

            boolean privateGame;
            if(playersNum==1){
                privateGame = true;
            } else {
                output.println("Is this a private game? [Y/n]");
                privateGame= input.nextLine().toLowerCase().startsWith("y");
            }
            if(username.trim().toLowerCase().equals("lorenzoilmagnifico")&&playersNum==1){
                displayError("There is only one 'Lorenzo Il Magnifico'");
                setup();
            }
            client.sendMessage(new CreateRoomMessage(privateGame, playersNum, username));
        }
        else {
            output.println("Do you have a RoomID or do you want to join a public game");
            selection = GameUtils.askIntegerInput("1: RoomId, 2: PublicGame", 1, 2, output, input);

            if (selection == 1) {
                int roomId = GameUtils.askIntegerInput("Insert roomId", 1000, 9999, output, input);
                client.sendMessage(new JoinPrivateRoomMessage(roomId, username));
            }
            else {
                int playersNumber = GameUtils.askIntegerInput("Insert desired number of players [0 for random]", 0, 4, output, input);
                client.sendMessage(new JoinPublicRoomMessage(playersNumber, username));
            }
        }
    }

    public void askUsername(){
        output.println("Enter your username: ");
        username = input.nextLine();
    }

    @Override
    public void displayError(String body) {
        output.println("ERROR!");
        output.println(body);
    }

    @Override
    public void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId) {
        Display.displayRoomDetails(players, playersNum, RoomId, output);
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
            int selection = GameUtils.askIntegerInput("select resource", 1, resources.size(), output, input);
            selectedResources.add(resources.get(selection-1));
        }

        client.sendMessage(new SelectInitialResourceResponseMessage(selectedResources));
    }

    @Override
    public void selectLeaderCards(ArrayList<LeaderCard> leaderCards) {
       Display.displayLeaderCards(leaderCards, output);
        int selection1;
        int selection2;
        do {
            selection1 = GameUtils.askIntegerInput("Select the first card to drop", 1,4, output, input)-1;
            selection2 = GameUtils.askIntegerInput("Select the second card to drop", 1,4, output, input)-1;
            if(selection1==selection2)output.println("You must select two distinct cards");
        } while (selection1 == selection2);

        client.sendMessage(new DropInitialLeaderCardsResponseMessage(selection1, selection2));

    }



    @Override
    public void displayGameState() {
        // displayPlayerBoard(gameState.getPlayerByUsername(username));

       Display.displayGameBoard(gameState.getMarket(), output);
       //momentary fix

        if(gameState.getCurrentPlayer()!= gameState.getPlayerByUsername(username)){
           for (Player player:
                gameState.getPlayers()) {
                   Display.displayPlayerBoard(gameState.getPlayerByUsername(player.getUsername()), output);
                   output.println("Wait your turn");
           }
       } else Display.displayPlayerBoard(gameState.getPlayerByUsername(username), output);

       // todo this in preferable to the above one but gives problems. Go Fix it
        /*
       if (gameState.getCurrentPlayer()!= gameState.getPlayerByUsername(username)) {
           executor.submit(this::askToDisplayPlayerBoard);
       }
         */
    }

    private void askToDisplayPlayerBoard(){
        output.println("Enter the username of a player you want to visit");
        output.print("[");
        for (Player player:
                gameState.getPlayers()) {
            output.print(player.getUsername() + "(" + player.getVP() + " points) ");
        }
        if(gameState.getLorenzoIlMagnifico() != null)output.print("LorenzoIlMagnifico (" + gameState.getLorenzoIlMagnifico().getVP() + ")");
        output.println("]");
        output.print("username: ");
        Player player;


            try {
                player = gameState.getPlayerByUsername(input.nextLine());
                if(player.getUsername().equals(username))Display.displayPlayerLeaderCards(player, output);
                Display.displayPlayerBoard(player, output);
            } catch (NoSuchElementException e) {
                output.println("Username not found");
            }
    }

    public void displayPossibleMoves(List<String> moves){
        output.println("Possible moves:");
        output.println("1. Visit a player");
        for (int i = 0; i < moves.size(); i++) {
            output.println((i+2)+". " + moves.get(i).toLowerCase());
        }

        int selection = GameUtils.askIntegerInput("Select a move", 1, moves.size()+1, output, input);
        if(selection==1){
            sendMove("VISIT_PLAYER");
            displayPossibleMoves(moves);
        }
        else sendMove(moves.get(selection-2));
    }

    private void sendMove(String move){
        if(move.equals("VISIT_PLAYER"))askToDisplayPlayerBoard();
        else client.sendMessage(new SelectMoveResponseMessage(move));
    }

    public void setGameState(Game game){
        gameState = game;
    }

    public void selectMarbles(MarbleStructure marbleStructure){
        Display.displayMarbleStructure(marbleStructure, output);

        int selection = GameUtils.askIntegerInput("Do you want to shift a row or a column?\n1.Row\n2.Column", 1,2, output, input);

        int selectionIndex;
        if (selection == 1){
            selectionIndex = GameUtils.askIntegerInput("Which row do you want to shift?", 1,3, output, input)-1;
            client.sendMessage(new SelectMarblesResponseMessage("ROW",selectionIndex));
        } else {
            selectionIndex = GameUtils.askIntegerInput("Which column do you want to shift?", 1,4, output, input)-1;
            client.sendMessage(new SelectMarblesResponseMessage("COLUMN",selectionIndex));
        }
    }

    @Override
    public void dropResources(Map<Resource, Integer> resources) {
        output.println("Choose which resources to drop:");
        Map<Resource, Integer> resourceMap = new LinkedHashMap<>(resources);
        output.println(Display.displayResources(resourceMap));
        List<Resource> uniqueResources = new ArrayList<>(resourceMap.keySet());
        for (int i = 0; i < uniqueResources.size(); i++) {
            output.print((i + 1) + ". " + uniqueResources.get(i).name() + " | ");
        }
        output.println((uniqueResources.size() + 1) + ". Confirm");

        Map<Resource, Integer> droppedResources = new HashMap<>();
        while (true) {
            // ask the user to select the type of resource to drop
            int selectedResourceIndex = GameUtils.askIntegerInput("Select a resource:",
                    1, uniqueResources.size() + 1, output, input) - 1;
            if (selectedResourceIndex == uniqueResources.size()) break;
            Resource selectedResource = uniqueResources.get(selectedResourceIndex);
            if (resourceMap.get(selectedResource) < 1) {
                output.println("Already dropped");
                continue;
            }
            // ask the user to tell how many units of the selected resource to drop
            int selectedAmount = GameUtils.askIntegerInput("Select quantity:",
                    1, resourceMap.get(selectedResource), output, input);

            resourceMap = GameUtils.incrementValueInResourceMap(resourceMap, selectedResource, -selectedAmount);
            droppedResources = GameUtils.incrementValueInResourceMap(droppedResources, selectedResource, selectedAmount);
        }
        client.sendMessage(new DropResourcesResponseMessage(droppedResources));
    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {
        Display.displayLeaderCards(cards, output);
        int selection = GameUtils.askIntegerInput("Select the card number", 1, cards.size(), output, input)-1;
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

                    selection = GameUtils.askIntegerInput(message,indexes.get(0),gameState.getCurrentPlayer().possibleProductionPowersToActive().size(), output, input);

                    if(indexes.contains(selection)) {

                        if (selection == 0) {
                            if (currentGameState.getCurrentPlayer().canActivateBasicProduction()) {
                                do{
                                    selectedBasicProductionPowers = askBasicProductionPowerIO();
                                    if(gameState.getCurrentPlayer().getPlayerBoard().canPayResources(selectedBasicProductionPowers.getInput())){
                                        currentGameState.getCurrentPlayer().getPlayerBoard().payResourceCost(selectedBasicProductionPowers.getInput());
                                        break;
                                    } else output.println("You do not have the selected input. Retry");
                                } while (true);
                            }
                        } else {
                            currentGameState.getCurrentPlayer().getPlayerBoard().payResourceCost(currentGameState.getCurrentPlayer().possibleProductionPowersToActive().get(selection - 1).getInput());
                            List<ProductionPower> possibleExtraPowers = gameState.getCurrentPlayer().getPlayerBoard().getExtraProductionPowers();
                            System.out.println(possibleExtraPowers);
                            if (possibleExtraPowers!= null && !possibleExtraPowers.isEmpty()
                                    && possibleExtraPowers.contains(currentGameState.getCurrentPlayer().possibleProductionPowersToActive().get(selection - 1))){
                                extraProductionPowers.add(selection - (gameState.getCurrentPlayer().possibleProductionPowersToActive().size()-1));
                            } else {
                                selectedStacks.add(selection - 1);
                            }

                        }
                        indexes.remove((Integer) selection);
                        productionNumber++;
                    } else output.println("Selection not valid");
                    if(indexes.size()>0){
                        output.println("Are you done? [y/n]");
                        done = input.nextLine().trim().toLowerCase().startsWith("y");
                    } else done = true;
                }
                client.sendMessage(new ActivateProductionResponseMessage(selectedStacks, selectedBasicProductionPowers, extraProductionPowers));


    }

    private ProductionPower askBasicProductionPowerIO(){
        Map<Resource, Integer> resInput = GameUtils.emptyResourceMap();
        Map<Resource, Integer> resOutput = GameUtils.emptyResourceMap();
        List<Integer> resources = new ArrayList<>();
        resources.add(GameUtils.askIntegerInput("What is the first resource you want to put as input?\n1.SHIELD, 2.SERVANT, 3.STONE, 4.COIN",1,4, output, input));
        resources.add(GameUtils.askIntegerInput("What is the second resource you want to put as input?\n1.SHIELD, 2.SERVANT, 3.STONE, 4.COIN",1,4, output, input));
        resources.add(GameUtils.askIntegerInput("What is the resource you want as output?\n1.SHIELD, 2.SERVANT, 3.STONE, 4.COIN",1,4, output, input));

        for (int i = 0; i < 3; i++) {
            switch (resources.get(i)){
                case (1):
                    if(i<2)resInput.put(Resource.SHIELD, resInput.get(Resource.SHIELD) + 1);
                    else resOutput.put(Resource.SHIELD,1);
                    break;
                case (2):
                    if(i<2)resInput.put(Resource.SERVANT,resInput.get(Resource.SERVANT) + 1);
                    else resOutput.put(Resource.SERVANT,1);
                    break;
                case (3):
                    if(i<2)resInput.put(Resource.STONE,resInput.get(Resource.STONE) + 1);
                    else resOutput.put(Resource.STONE,1);
                    break;
                case (4):
                    if(i<2)resInput.put(Resource.COIN, resInput.get(Resource.COIN) + 1);
                    else resOutput.put(Resource.COIN,1);
                    break;
            }
        }
        return new ProductionPower(resInput, resOutput);
    }

    @Override
    public void buyDevelopmentCard(List<DevelopmentCard> developmentCards) {
        int selection;
        for (DevelopmentCard developmentCard:
             developmentCards) {
            output.println(Display.paintCard(developmentCard.getCardType()) +  developmentCard + "\u001B[0m");
        }
        selection = GameUtils.askIntegerInput("Select a card", 1, developmentCards.size(), output, input)-1;
        client.sendMessage(new BuyDevelopmentCardResponseMessage(selection));

    }

    @Override
    public void selectStackToPlaceCard(List<Integer> stackIndexes) {

        for (Integer index:
             stackIndexes) {
            System.out.println(stackIndexes.indexOf(index)+1 + ":");

            PlayerCardStack playerCardStack = gameState.getCurrentPlayer().getPlayerBoard().getCardStacks().get(index);
            if(playerCardStack.isEmpty())output.println("Empty");
            else output.println(Display.paintCard(playerCardStack.peek().getCardType()) + playerCardStack + "\u001B[0m");
        }
        int selection = GameUtils.askIntegerInput("On which stack you want to put your new card?", 1, stackIndexes.size(), output, input)-1;
        client.sendMessage(new SelectStackToPlaceCardResponseMessage(stackIndexes.get(selection)));
    }

    @Override
    public void playLeader(List<LeaderCard> leaderCards) {
        Display.displayLeaderCards(leaderCards, output);
        int selection = GameUtils.askIntegerInput("Select the card number", 1, leaderCards.size(), output, input)-1;
        client.sendMessage(new PlayLeaderResponseMessage(selection));
    }

    public void gameOver(String winner, Map<String, Integer> standing){
        output.println("Game ended, "+winner+" won the game\n"+standing);
        int selection = GameUtils.askIntegerInput("Do you want to start a new game?", 1,2, output, input);
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

    public String getUsername() {
        return username;
    }
}
