package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.LocalClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerCardStack;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.network.game.*;
import it.polimi.ingsw.network.setup.CreateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPrivateRoomMessage;
import it.polimi.ingsw.network.setup.JoinPublicRoomMessage;
import it.polimi.ingsw.utils.GameUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;

public class CLI implements UI {
    private Client client;
    private final Scanner input;
    private final PrintStream output;
    private String username;
    private Game gameState;
    private Consumer lastFunction;

    public CLI() {
        input = new Scanner(System.in);
        output= new PrintStream(System.out);
    }

    @Override
    public void run() {
        Display.displayWelcomeMessage(output);
        output.println("How do you want to play?");
        int selection = GameUtils.askIntegerInput("1. online | 2. offline", 1, 2, output, input);
        switch (selection) {
            case 1:
                this.client = new Client(this);
                try {
                    output.println("Write Server IP");
                    String server = input.nextLine();
                    output.println("Write Server Port");
                    Integer port = Integer.parseInt(input.nextLine());
                    if(server.equals("")){
                        //this is just for make easy testing by playing on local host
                        this.client.connect("localhost", 31415);
                    } else
                    {
                        this.client.connect(server, port);
                    }

                    setup();
                } catch (IOException e) {
                    output.println("Server unavailable :(");
                    output.println("exiting...");
                    System.exit(404);
                    return;
                }
                break;
            case 2:
                this.client = new LocalClient(this);
                this.client.run();
                break;
        }
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
            if(username.trim().equalsIgnoreCase("lorenzoilmagnifico")&&playersNum==1){
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
        output.println(Display.paint("RED","ERROR!" ));
        output.println(Display.paint("RED", body));
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
        Display.displayGameBoard(gameState.getMarket(), output);
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
            selection1 = GameUtils.askIntegerInput("Select the first card to " + Display.paint("RED","drop"), 1,4, output, input)-1;
            selection2 = GameUtils.askIntegerInput("Select the second card to " + Display.paint("RED","drop"), 1,4, output, input)-1;
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
                gameState.getActivePlayers()) {
                   Display.displayPlayerBoard(gameState.getPlayerByUsername(player.getUsername()), output);
               if(gameState.getActivePlayers().get(gameState.getInkwellPlayer()).getUsername().equals(player.getUsername()))output.println("Inkwell");
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
                gameState.getActivePlayers()) {
            output.print(player.getUsername() + "(" + player.getVP() + " points) ");
        }
        if(gameState.getLorenzoIlMagnifico() != null)output.print("LorenzoIlMagnifico (" + gameState.getLorenzoIlMagnifico().getVP() + ")");
        output.println("]");
        output.print("username: ");
        Player player;

            try {
                do{
                    player = gameState.getPlayerByUsername(input.nextLine());
                } while (player==null);
                if(player.getUsername().equals(username))Display.displayPlayerLeaderCards(player, output);
                else if (player != gameState.getLorenzoIlMagnifico())Display.displayLeaderCards(player.getPlayedLeaderCards(), output);
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
        output.println(Display.displayResourceMap(resourceMap));
        List<Resource> uniqueResources = new ArrayList<>(resourceMap.keySet());
        for (int i = 0; i < uniqueResources.size(); i++) {
            output.print((i + 1) + ". " + Display.displayResourceType(uniqueResources.get(i)) + " | ");
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
    public void selectResourceForWhiteMarbles(int amount, List<Resource> options) {
        output.println("You have " + amount + " white marbles to convert.");
        Map<Resource, Integer> converted = GameUtils.emptyResourceMap();
        for (int i = 0; i < amount; i++) {
            output.println("Convert marble number " + (i+1));
            int selection = GameUtils.askIntegerInput(
                    "1. " + options.get(0) + " | 2. "+ options.get(1), 1, 2, output, input);
            GameUtils.incrementValueInResourceMap(converted, options.get(selection - 1), 1);
        }
        client.sendMessage(new SelectResourceForWhiteMarbleResponseMessage(converted));
    }

    @Override
    public void discardLeaderCard(ArrayList<LeaderCard> cards) {
        Display.displayLeaderCards(cards, output);
        int selection = GameUtils.askIntegerInput("Select the card number", 1, cards.size(), output, input)-1;
        client.sendMessage(new DropLeaderCardResponseMessage(selection));
    }



    @Override
    public void switchShelves(ArrayList<String> shelves) {
        Display.displayShelves(shelves, gameState.getCurrentPlayer().getPlayerBoard().getWarehouse() , output);
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
        List<Resource> extraOutputs = new ArrayList<>();
        int size = productionPowers.size();

                if(currentGameState.getCurrentPlayer().canActivateBasicProduction()){
                    indexes.add(0);
                }
                for (int i = 1; i <= productionPowers.size(); i++) {
                    indexes.add(i);
                }
                int productionNumber=1;
                while (!done){
                    System.out.println(gameState.getCurrentPlayer().getPlayerBoard().getResources());
                    output.println(gameState.getCurrentPlayer().possibleProductionPowersToActive());
                    String message;
                    message ="Select production power N° " +productionNumber+" to activate" ;
                    if(indexes.contains(0) && gameState.getCurrentPlayer().canActivateBasicProduction()){
                        message+="[0 is basic production]";
                    }
                    message+="\n"+ indexes;

                    do{
                        selection = GameUtils.askIntegerInput(message,indexes.get(0),size, output, input);
                    } while (!indexes.contains(selection));

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
                            } else output.println(Display.paint("RED", "Production power no longer valid"));
                        } else {
                            if(currentGameState.getCurrentPlayer().getPlayerBoard().canPayResources(productionPowers.get(selection-1).getInput())){
                                currentGameState.getCurrentPlayer().getPlayerBoard().payResourceCost(productionPowers.get(selection - 1).getInput());
                                List<ProductionPower> possibleExtraPowers = gameState.getCurrentPlayer().getPlayerBoard().getExtraProductionPowers();
                                System.out.println(possibleExtraPowers);
                                if (possibleExtraPowers!= null && !possibleExtraPowers.isEmpty()
                                        && productionPowers.get(selection-1).getOutput().containsKey(Resource.ANY)
                                       /* && selection>productionPowers.size()-possibleExtraPowers.size()*//* possibleExtraPowers.contains(productionPowers.get(selection - 1))*/){
                                    if(possibleExtraPowers.size()!=1 && selection==productionPowers.size())extraProductionPowers.add(1);
                                    else extraProductionPowers.add(0);
                                    //extraProductionPowers.add(selection - (productionPowers.size()));
                                    extraOutputs.add(askExtraOutput());
                                } else {
                                    selectedStacks.add(selection - 1);
                                }
                                productionNumber++;
                            } else output.println(Display.paint("RED", "Production power no longer valid"));
                        }
                        indexes.remove(selection);

                    } else output.println("Selection not valid");
                    if(indexes.size()>0){
                        output.println("Are you done? [y/n]");
                        done = input.nextLine().trim().toLowerCase().startsWith("y");
                    } else done = true;
                }
                GameUtils.debug(selectedStacks.toString());
                client.sendMessage(new ActivateProductionResponseMessage(selectedStacks, selectedBasicProductionPowers, extraProductionPowers, extraOutputs));
    }

    private Resource askExtraOutput(){
        Resource resource = Resource.ANY;
        int selection;
        List<Resource> allRes = new ArrayList<>();
        allRes.add(Resource.SHIELD);
        allRes.add(Resource.SERVANT);
        allRes.add(Resource.STONE);
        allRes.add(Resource.COIN);


        selection = GameUtils.askIntegerInput("Which resource do you want in output?\n"+ Display.displayResourceList(allRes), 1, 4, output, input)-1;

        resource = allRes.get(selection);

        return resource;
    }

    private ProductionPower askBasicProductionPowerIO(){
        Map<Resource, Integer> resInput = GameUtils.emptyResourceMap();
        Map<Resource, Integer> resOutput = GameUtils.emptyResourceMap();
        List<Integer> resources = new ArrayList<>();

        List<Resource> availableResources = new ArrayList<>();
        for (Resource r:
             gameState.getCurrentPlayer().getPlayerBoard().getResources().keySet()) {
            if(gameState.getCurrentPlayer().getPlayerBoard().getResources().get(r)>0)availableResources.add(r);
        }
        List<Resource> allRes = new ArrayList<>();
        allRes.add(Resource.STONE);
        allRes.add(Resource.COIN);
        allRes.add(Resource.SERVANT);
        allRes.add(Resource.SHIELD);

        output.println("These are your available resources " + Display.displayResourceList(availableResources));
        resources.add(GameUtils.askIntegerInput("What is the first resource you want to put as input?",1,availableResources.size(), output, input)-1);
        resources.add(GameUtils.askIntegerInput("What is the second resource you want to put as input?",1,availableResources.size(), output, input)-1);
        resources.add(GameUtils.askIntegerInput("What is the resource you want as output?"+"\n" +  Display.displayResourceList(allRes),1,allRes.size(), output, input)-1);

        resInput.put(availableResources.get(resources.get(0)), 1);
        resInput.put(availableResources.get(resources.get(1)), resInput.get(availableResources.get(resources.get(1)))+1);
        resOutput.put(allRes.get(resources.get(2)), 1);

        return new ProductionPower(resInput, resOutput);
    }

    @Override
    public void buyDevelopmentCard(List<DevelopmentCard> developmentCards) {
        System.out.println(developmentCards.size());
        int selection;
        for (DevelopmentCard developmentCard:
             developmentCards) {
            output.println((developmentCards.indexOf(developmentCard)+1)+":");
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
        output.println(Display.paint("YELLOW", "Game ended, "+winner+" won the game"));
        output.println(standing);
        int selection = GameUtils.askIntegerInput("Do you want to start a new game?[1.yes 2.no]", 1,2, output, input);
        if(selection==1){
            run();
        }
        else {
            output.println("Bye "+username);
            try {
                client.closeConnection();
            } catch (NullPointerException e){

            }
            System.exit(0);
        }
    }

    public String getUsername() {
        return username;
    }


}
