package it.polimi.ingsw.view;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.shared.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class Display {

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

    public static void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId, PrintStream output) {
        output.println("Game details:");
        output.println("🧑🏻‍💻" + players);
        output.println("🔢" + playersNum);
        output.println("⌘" + RoomId);
    }

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

    public static void displayGameBoard(Market market, PrintStream output){
        displayMarbleStructure(market.getMarbleStructure(), output);
        System.out.println("\nCards Market:\n");


        /*
        for (int i = 0; i < 12; i+=4) {
            displayCardsLine(market, i, output);
        }

         */

        for (int i = 0; i < 12; i++) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else displayDevelopmentCard(market, i, output);
        }

    }

    public static void displayPlayerBoard(Player player, PrintStream output){
        output.println("🧑🏻‍💻" + player.getUsername() + " playerBoard: ");
        output.println("\n ✝ Faith track positions");
        output.println("position: " + player.getFaithTrack().getPosition());
        for (int i = 0; i < 3; i++) {
            PopesFavorTile popesFavorTile = player.getFaithTrack().getPopesFavorTiles().get(i);
            output.print("Pope's favor tile level "+ (i+1) + ": ");
            displayPopeTyle(popesFavorTile, output);

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

    private static void displayPopeTyle(PopesFavorTile popesFavorTile, PrintStream output){
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

    private static void displayStrongbox(Strongbox strongbox, PrintStream output) {
        Map<Resource, Integer> resources = strongbox.getResources();
        output.println(displayResourceType(Resource.COIN) +": " +resources.get(Resource.COIN) + " " + displayResourceType(Resource.SERVANT) +": " + resources.get(Resource.SERVANT) + " " + displayResourceType(Resource.STONE) +": " + resources.get(Resource.STONE)+ " " + displayResourceType(Resource.SHIELD)+": " + resources.get(Resource.SHIELD));
    }

    private static void displayDevelopmentCard(Market market, int i, PrintStream output){
        output.print(paintCard(market.getCardsGrid().get(i).getType()));
        output.println(market.getCardsGrid().get(i));
        output.print("\u001B[0m");
        output.println("\n");
    }

    public static String displayCardType(CardType cardType){
        switch (cardType){
            case YELLOW:
                return "🟨";
            case PURPLE:
                return "🟪";
            case BLUE:
                return "🟦";
            case GREEN:
                return "🟩";
        }
        return "";
    }

    private static void displayCardsLine(Market market, int i, PrintStream output){
        displayCardBorder(output);
        output.println("|" + paintCard(market.getCardsGrid().get(i).getType()) +  normalizeCardField("level: " + market.getCardsGrid().get(i).peek().getLevel()) +"\u001B[0m" + "|" +"           " +"|" + paintCard(market.getCardsGrid().get(i+1).getType()) +  normalizeCardField("level: " + market.getCardsGrid().get(i+1).peek().getLevel()) +"\u001B[0m" + "|" +"           " + "|" +paintCard(market.getCardsGrid().get(i+2).getType()) +  normalizeCardField("level: " + market.getCardsGrid().get(i+2).peek().getLevel()) +"\u001B[0m" +"|" + "           " + "|" +paintCard(market.getCardsGrid().get(i+3).getType()) + normalizeCardField("level: " + market.getCardsGrid().get(i+3).peek().getLevel()) +"\u001B[0m" +"|" + "           ");
        output.println("|" + paintCard(market.getCardsGrid().get(i).getType()) + normalizeCardField(displayCost(market.getCardsGrid().get(i).peek().getCost()))+"\u001B[0m" +"|" +"           " + "|" + paintCard(market.getCardsGrid().get(i+1).getType()) + normalizeCardField(displayCost(market.getCardsGrid().get(i+1).peek().getCost()))+"\u001B[0m" + "|" +"           " + "|" + paintCard(market.getCardsGrid().get(i+2).getType()) +  normalizeCardField(displayCost(market.getCardsGrid().get(i+2).peek().getCost())) +"\u001B[0m" + "|" +"           " + "|" + paintCard(market.getCardsGrid().get(i+3).getType()) + normalizeCardField(displayCost(market.getCardsGrid().get(i+3).peek().getCost())) +"\u001B[0m" + "|" +"           ");
        output.println("|" + paintCard(market.getCardsGrid().get(i).getType()) +  normalizeCardField(market.getCardsGrid().get(i).peek().getProductionPower().toString()) +"\u001B[0m" + "|" + "           " +"|" + paintCard(market.getCardsGrid().get(i+1).getType()) +  normalizeCardField(market.getCardsGrid().get(i+1).peek().getProductionPower().toString()) +"\u001B[0m" + "|" + "           " +"|" + paintCard(market.getCardsGrid().get(i+2).getType()) +  normalizeCardField(market.getCardsGrid().get(i+2).peek().getProductionPower().toString()) +"\u001B[0m" +"|" + "           "+"|" +paintCard(market.getCardsGrid().get(i+3).getType()) +  normalizeCardField(market.getCardsGrid().get(i+3).peek().getProductionPower().toString()) + "\u001B[0m" + "|" + "           ");
        output.println("|" + paintCard(market.getCardsGrid().get(i).getType()) +  normalizeCardField(String.valueOf(market.getCardsGrid().get(i).peek().getScore())) +"\u001B[0m" + "|" +"           " +"|" + paintCard(market.getCardsGrid().get(i+1).getType()) +  normalizeCardField(String.valueOf(market.getCardsGrid().get(i+1).peek().getScore())) +"\u001B[0m" + "|" +"           " + "|" +paintCard(market.getCardsGrid().get(i+2).getType()) +  normalizeCardField(String.valueOf(market.getCardsGrid().get(i+2).peek().getScore())) +"\u001B[0m" +"|" + "           " + "|" +paintCard(market.getCardsGrid().get(i+3).getType()) + normalizeCardField(String.valueOf(market.getCardsGrid().get(i+3).peek().getScore())) +"\u001B[0m" +"|" + "           ");
        displayCardBorder(output);
    }


    private static void displayCardBorder(PrintStream output){
        for (int j = 0; j < 4; j++) {
            output.print(" ");
            for (int k = 0; k < 50; k++) {
                output.print("-");
            }
            output.print("           ");
        }
        output.println();
    }
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

    public static String displayResourceMap(Map<Resource, Integer> resourceMap) {
        String output = "";

        for (Resource r : resourceMap.keySet()) {
            output += resourceMap.get(r) + "x" + displayResourceType(r) + " ";
        }
        return output;
    }

    public static String displayResourceList(List<Resource> resources){
        String result = "";
        for (Resource r:
             resources) {
            result += (resources.indexOf(r)+1) + "." + displayResourceType(r) + " ";
        }
        return result;
    }

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

    public static void displayLeader(LeaderCard leaderCard, PrintStream output){
        output.print("\u001B[31m");
        output.print(leaderCard.printResourceRequirements());
        output.print(leaderCard.printCardRequirements());
        output.print("Score: " + leaderCard.getScore() + "\n" + "Effect: " +leaderCard.getEffectScope() + " " +leaderCard.getEffectObject() + "\n");
        output.print("\u001B[0m");
    }

    public static void displayLeaderCards(List<LeaderCard> leaderCards, PrintStream output){
        for (LeaderCard leader:
                leaderCards) {
            output.println("card number "+(leaderCards.indexOf(leader)+1));
            Display.displayLeader(leader, output);
            output.println();
        }
    }



    public static String normalizeCardField(String input){
        //System.out.println(input.length());
        String result = input;
        int normalizedSize = 50;
        for (int i = input.length(); i <= normalizedSize; i++) {
            result += " ";
        }

        //System.out.println(result.length());
        return result;
    }

    private static String displayCost(Map<Resource, Integer> cost){
        String result = "cost: ";
        for (Resource resource:
             cost.keySet()) {
            if(cost.get(resource) > 0){
                result += resource.toString();
                result += " ";
                result += cost.get(resource).toString();
                result += ", ";
            }
        }
        return result;
    }

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
}
