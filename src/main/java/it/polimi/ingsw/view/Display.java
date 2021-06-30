package it.polimi.ingsw.view;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.shared.*;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Display class, used to print infos on the screen, it has statics methods that takes an output on which they have to
 * print.
 */
public class Display {

    /**
     * Displays a welcome message
     */
    public static void displayWelcomeMessage(PrintStream output){
        output.println(
                "+-----------------------------------------------------------------------------------------+\n" +
                        "|                                                                                         |\n" +
                        "| ███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗     ██████╗ ███████╗       |\n" +
                        "| ████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝    ██╔═══██╗██╔════╝       |\n" +
                        "| ██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗    ██║   ██║█████╗         |\n" +
                        "| ██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║    ██║   ██║██╔══╝         |\n" +
                        "| ██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║    ╚██████╔╝██║            |\n" +
                        "| ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝            |\n" +
                        "|                                                                                         |\n" +
                        "| ██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗ |\n" +
                        "| ██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝ |\n" +
                        "| ██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗   |\n" +
                        "| ██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝   |\n" +
                        "| ██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗ |\n" +
                        "| ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝ |\n" +
                        "|                                                                                         |\n" +
                        "| developed by Bruno Morelli, Samuele Messineo and Alberto Mosconi                        |\n" +
                        "+-----------------------------------------------------------------------------------------+");
    }

    /**
     * Displays the room details.
     * @param players the list of players.
     * @param playersNum the number of players.
     * @param RoomId the id of the room.
     */
    public static void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId, PrintStream output) {
        output.println("Game details:");
        output.println("🧑🏻‍💻" + players);
        output.println("🔢" + playersNum);
        output.println("⌘" + RoomId);
    }

    /**
     * Displays the marble structure.
     * @param marbleStructure a MarbleStructure
     */
    public static void displayMarbleStructure(MarbleStructure marbleStructure, PrintStream output){
        String result = "Marble Structure: \n";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                result += displayMarble(marbleStructure.getMarbles().get((i*4)+j)) + " ";
            }
            result += "\n";
        }
        result += "Extra Marble: " + displayMarble(marbleStructure.getExtraMarble());
        output.println(result);
    }

    /**
     * Displays the marble as a colored dot
     * @param marble a Marble
     * @return a colored dot
     */
    private static String displayMarble(Marble marble){
        switch (marble){
            case BLUE:
                return "\u001B[34m⬤\u001B[0m";
            case RED:
                return "\u001B[31m⬤\u001B[0m";
            case PURPLE:
                return "\u001B[35m⬤\u001B[0m";
            case YELLOW:
                return "\u001B[33m⬤\u001B[0m";
            case GREY:
                return "\u001B[37m⬤\u001B[0m";
            case WHITE:
                return "\u001B[0m⬤\u001B[0m";
        }
        return "";
    }

    /**
     * Display the Resource image associated with the resource.
     * @param resource a Resource.
     * @return the image associated to the resource.
     */
    public static String displayResourceType(Resource resource){
        switch (resource){

            case COIN:
                return "🟡";
            case FAITH:
                return "✝";
            case SERVANT:
                return "🧞";
            case SHIELD:
                return "🛡";
            case STONE:
                return "🪨️";
            case ANY:
                return "🃏️";
        }
        return "";
    }

    /**
     * Displays the game board.
     * @param market a Market
     */
    public static void displayGameBoard(Market market, PrintStream output){
        displayMarbleStructure(market.getMarbleStructure(), output);
        output.println("\nCards Market:");
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                output.print(paintCard(card.getCardType()));
                output.print("+----------------+ ");
                output.print("\u001B[0m");
            }
            output.print("\n");
            int costRows = 0;
            int inputRows = 0;
            int outputRows = 0;
            for (int col = 0; col < 4; col++) {
                DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                int nc = (int)card.getCost().keySet().stream().filter(k -> card.getCost().get(k) > 0).count();
                if (nc > costRows) costRows = nc;
                int ni = card.getProductionPower().getInput().keySet().size();
                if (ni > inputRows) inputRows = ni;
                int no = card.getProductionPower().getOutput().keySet().size();
                if (no > outputRows) outputRows = no;
                // 15
                output.print(paintCard(card.getCardType()));
                output.printf("|  Level: %d      | ", card.getLevel());
                output.print("\u001B[0m");
            }
            output.print("\n");
            for (int cr = -1; cr < costRows; cr++) {
                for (int col = 0; col < 4; col++) {
                    DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                    output.print(paintCard(card.getCardType()));
                    if (cr == -1)
                        output.printf("|  %-14s| ", "Cost:");
                    else {
                        // 15
                        List<Resource> keys = card.getCost().keySet().stream().filter(k -> card.getCost().get(k) > 0).sorted((o1, o2) -> card.getCost().get(o2) - card.getCost().get(o1)).collect(Collectors.toList());
                        if (cr < keys.size()) {
                            output.printf("|    %dx %-9s| ", card.getCost().get(keys.get(cr)), keys.get(cr).name());
                        } else {
                            output.print("|                | ");
                        }
                    }
                    output.print("\u001B[0m");
                }
                output.print("\n");
            }
            for (int ir = -1; ir < inputRows; ir++) {
                for (int col = 0; col < 4; col++) {
                    DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                    output.print(paintCard(card.getCardType()));
                    if (ir == -1)
                        output.printf("|  %-14s| ", "Input:");
                    else {
                        // 15
                        List<Resource> keys = new ArrayList<>(card.getProductionPower().getInput().keySet());
                        if (ir < keys.size()) {
                            output.printf("|    %dx %-9s| ", card.getProductionPower().getInput().get(keys.get(ir)), keys.get(ir).name());
                        } else {
                            output.print("|                | ");
                        }
                    }
                    output.print("\u001B[0m");
                }
                output.print("\n");
            }
            for (int ir = -1; ir < outputRows; ir++) {
                for (int col = 0; col < 4; col++) {
                    DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                    output.print(paintCard(card.getCardType()));
                    if (ir == -1)
                        output.printf("|  %-14s| ", "Output:");
                    else {
                        // 15
                        List<Resource> keys = new ArrayList<>(card.getProductionPower().getOutput().keySet());
                        if (ir < keys.size()) {
                            output.printf("|    %dx %-9s| ", card.getProductionPower().getOutput().get(keys.get(ir)), keys.get(ir).name());
                        } else {
                            output.print("|                | ");
                        }
                    }
                    output.print("\u001B[0m");
                }
                output.print("\n");
            }
            for (int col = 0; col < 4; col++) {
                DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                // 15
                output.print(paintCard(card.getCardType()));
                output.printf("|  %-14s| ", "Score: "+card.getScore());
                output.print("\u001B[0m");
            }
            output.print("\n");
            for (int col = 0; col < 4; col++) {
                DevelopmentCard card = market.getCardsGrid().get((row * 4) + col).peek();
                output.print(paintCard(card.getCardType()));
                output.print("+----------------+ ");
                output.print("\u001B[0m");
            }
            output.print("\n");
        }
    }

    /**
     * Displays the board of a player.
     * @param player the Player of which the board has to be displayed.
     */
    public static void displayPlayerBoard(Player player, PrintStream output){
        output.println("🧑🏻‍💻" + player.getUsername() + " playerBoard: ");
        output.println("\n ✝ Faith track positions");
        output.println("position: " + player.getFaithTrack().getPosition());
        for (int i = 0; i < 3; i++) {
            PopesFavorTile popesFavorTile = player.getFaithTrack().getPopesFavorTiles().get(i);
            output.print("Pope's favor tile level "+ (i+1) + ": ");
            displayPopeTile(popesFavorTile, output);
        }
        output.println("\n 🏦 Storage");
        displayWarehouse(player.getPlayerBoard().getWarehouse(), output);
        output.print("strongbox: ");
        displayStrongbox(player.getPlayerBoard().getStrongbox(), output);
        output.println("\n 🂡 Cards");
        for (int i = 0; i < 3 ; i++) {
            PlayerCardStack developmentCard = player.getPlayerBoard().getCardStacks().get(i);
            if(developmentCard.isEmpty())output.println(developmentCard.toString());
            else output.println(paintCard(developmentCard.peek().getCardType()) + developmentCard.toString() + "\u001B[0m");
        }
        output.println();
    }

    /**
     * Displays a Warehouse.
     * @param warehouse a Warehouse
     */
    private static void displayWarehouse(Warehouse warehouse, PrintStream output){
        output.println("    " + displayResourceType(warehouse.getShelf("top").getResourceType()));
        output.print("   ");
        for (int i = 0; i < warehouse.getShelf("middle").getResourceNumber(); i++) output.print(displayResourceType(warehouse.getShelf("middle").getResourceType()));
        for (int i = warehouse.getShelf("middle").getResourceNumber(); i < 2; i++ ) output.print(displayResourceType(Resource.ANY));
        output.println();
        output.print("  ");
        for (int i = 0; i < warehouse.getShelf("bottom").getResourceNumber(); i++) output.print(displayResourceType(warehouse.getShelf("bottom").getResourceType()));
        for (int i = warehouse.getShelf("bottom").getResourceNumber(); i < 3; i++ ) output.print(displayResourceType(Resource.ANY));
        output.println();
        if(warehouse.getShelf("extra1")!=null){
            output.print("   ");
            for (int i = 0; i < warehouse.getShelf("extra1").getResourceNumber(); i++) output.print(displayResourceType(warehouse.getShelf("extra1").getResourceType()));
            for (int i = warehouse.getShelf("extra1").getResourceNumber(); i < 2; i++ ) output.print(displayResourceType(Resource.ANY));
            output.print("  ("+displayResourceType(warehouse.getShelf("extra1").getResourceType())+")");
            output.println();
        }
        if(warehouse.getShelf("extra2")!=null){
            output.print("   ");
            for (int i = 0; i < warehouse.getShelf("extra2").getResourceNumber(); i++) output.print(displayResourceType(warehouse.getShelf("extra2").getResourceType()));
            for (int i = warehouse.getShelf("extra2").getResourceNumber(); i < 2; i++ ) output.print(displayResourceType(Resource.ANY));
            output.print("  ("+displayResourceType(warehouse.getShelf("extra2").getResourceType())+")");
            output.println();
        }
    }

    /**
     * Displays a yellow empty box if the Tile is inactive, a green 'tic' if it is active, a red cross if it is inactive.
     * @param popesFavorTile a PopeFavorTile.
     */
    private static void displayPopeTile(PopesFavorTile popesFavorTile, PrintStream output){
        switch (popesFavorTile.getState()){
            case INACTIVE:
                output.println(paint("YELLOW", "☐"));
                break;
            case ACTIVE:
                output.println(paint("GREEN", "☑"));
                break;
            case DISCARDED:
                output.println(paint("RED", "☒"));
                break;

        }
    }

    /**
     * Displays a strongbox.
     * @param strongbox a Strongbox
     */
    private static void displayStrongbox(Strongbox strongbox, PrintStream output) {
        Map<Resource, Integer> resources = strongbox.getResources();
        output.println(displayResourceType(Resource.COIN) +": " +resources.get(Resource.COIN) + " " + displayResourceType(Resource.SERVANT) +": " + resources.get(Resource.SERVANT) + " " + displayResourceType(Resource.STONE) +": " + resources.get(Resource.STONE)+ " " + displayResourceType(Resource.SHIELD)+": " + resources.get(Resource.SHIELD));
    }

    /**
     * Display a colored card.
     * @param cardType the type of the card.
     * @return the color of the card, it has to be prepended to the card.
     */
    public static String paintCard(CardType cardType){
        switch (cardType){
            case GREEN:
                return "\u001B[32m";
            case BLUE:
                return "\u001B[34m";
            case PURPLE:
                return "\u001B[35m";
            case YELLOW:
                return "\u001B[33m";
        }
        return "\u001B[0m";
    }

    /**
     * Displays a resourceMap.
     * @param resourceMap a Map<Resource, Integer>.
     */
    public static String displayResourceMap(Map<Resource, Integer> resourceMap) {
        String output = "";

        for (Resource r : resourceMap.keySet()) {
            output += resourceMap.get(r) + "x" + displayResourceType(r) + " ";
        }
        return output;
    }

    /**
     * Displays a list of resources.
     * @param resources a List of resources.
     */
    public static String displayResourceList(List<Resource> resources){
        String result = "";
        for (Resource r:
             resources) {
            result += (resources.indexOf(r)+1) + "." + displayResourceType(r) + " ";
        }
        return result;
    }

    /**
     * Displays PLayer Leader Cards.
     * @param player a Player.
     */
    public static void displayPlayerLeaderCards(Player player, PrintStream output){
        output.println("Not yet used leader cards:");
        if(player.getLeaderCards().size()==0)output.println("\u001B[31m" + "none" + "\u001B[0m" );
        for (LeaderCard leader:
             player.getLeaderCards()) {
            displayLeader(leader, output);
        }
        output.println("Played leader cards:");
        if(player.getPlayedLeaderCards().size()==0)output.println("\u001B[31m" + "none" + "\u001B[0m" );
        for (LeaderCard leader:
                player.getPlayedLeaderCards()) {
            displayLeader(leader, output);
        }
    }

    /**
     * Displays a single leaderCard.
     * @param leaderCard a LeaderCard.
     */
    public static void displayLeader(LeaderCard leaderCard, PrintStream output){
        output.print("\u001B[31m");
        output.print(leaderCard.printResourceRequirements());
        output.print(leaderCard.printCardRequirements());
        output.print("Score: " + leaderCard.getScore() + "\n" + "Effect: " +leaderCard.getEffectScope() + " " +leaderCard.getEffectObject() + "\n");
        output.print("\u001B[0m");
    }

    /**
     * Displays a list of leader cards.
     * @param leaderCards a list of leader cards.
     */
    public static void displayLeaderCards(List<LeaderCard> leaderCards, PrintStream output){
        for (LeaderCard leader:
                leaderCards) {
            output.println("card number "+(leaderCards.indexOf(leader)+1));
            Display.displayLeader(leader, output);
            output.println("\n");
        }
    }

    /**
     * Paints a string.
     * @param color the color of which the string has to been be painted, can be: GREEN, BLUE, PURPLE, YELLOW or RED.
     * @param input the input String.
     * @return the painted String.
     */
    public static String paint(String color, String input){
        String colorOut = "";
        switch (color){
            case "GREEN":
                colorOut = "\u001B[32m";
                break;
            case "BLUE":
                colorOut = "\u001B[34m";
                break;
            case "PURPLE":
                colorOut = "\u001B[35m";
                break;
            case "YELLOW":
                colorOut = "\u001B[33m";
                break;
            case "RED":
                colorOut = "\u001B[31m";
        }
        String result = "";
        result += colorOut;
        result += input;
        result += "\u001B[0m";
        return result;
    }

    /**
     * Displays a list of shelves.
     * @param shelvesNames a List of shelves names.
     * @param warehouse a Warehouse.
     */
    public static void displayShelves(List<String> shelvesNames, Warehouse warehouse, PrintStream output){
        output.println("\nWAREHOUSE:");
        for(String name: shelvesNames){
            output.println(name+": "+warehouse.getShelf(name));
        }
    }
}
