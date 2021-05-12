package it.polimi.ingsw.view;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.Resource;

import java.io.PrintStream;
import java.util.ArrayList;

public class Display {


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
                return "🔵";
            case RED:
                return  "🔴";
            case PURPLE:
                return "🟣";
            case YELLOW:
                return "🟡";
            case GREY:
                return "⚫️";
            case WHITE:
                return "⚪️";
        }
        return "";
    }

    private static String displayResource(Resource resource){
        switch (resource){

            case COIN:
                return "🟡";
            case FAITH:
                return "🔴";
            case SERVANT:
                return "🟣";
            case SHIELD:
                return "🔵";
            case STONE:
                return "⚫️";
            case ANY:
                return "⚪️";
        }
        return "";
    }

    public static void displayGameBoard(Market market, PrintStream output){
        displayMarbleStructure(market.getMarbleStructure(), output);
        System.out.println("\nCards Market:\n");

        /*
        for (int i = 11; i > 8; i--) {
            displayCardsLine(market, i, output);
        }

         */

        for (int i = 0; i < 12; i++) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else displayDevelopmentCard(market, i, output);
        }

        /*
        for (int i = 11; i >= 0; i-=3) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else {
                displayDevelopmentCard(market, i, output);
            }

        }
        for (int i = 1; i <= 10; i+=3) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else {
                displayDevelopmentCard(market, i, output);
            }
        }
        for (int i = 9; i >= 0; i-=3) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else {
                displayDevelopmentCard(market, i, output);
            }
        }
        */


    }

    public static void displayPlayerBoard(Player player, PrintStream output){
        output.println("🧑🏻‍💻" + player.getUsername() + " playerBoard: ");
        output.println("\n ✝ Faith track positions");
        output.println("position: " + player.getFaithTrack().getPosition());
        output.println("\n 🏦 Storage");
        output.println(player.getPlayerBoard());
        output.println("\n 🂡 Cards");
        for (int i = 0; i < 3 ; i++) {
            output.println(player.getPlayerBoard().getCardStacks().get(i).toString());
        }
        output.println();
    }

    private static void displayDevelopmentCard(Market market, int i, PrintStream output){
        output.print(paintCard(market.getCardsGrid().get(i).getType()));
        output.println(market.getCardsGrid().get(i));
        output.print("\u001B[0m");
        output.println("\n");
    }

    private static void displayCardsLine(Market market, int i, PrintStream output){
        output.println(paintCard(market.getCardsGrid().get(i).getType()) +  market.getCardsGrid().get(i).peek().getCost() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-3).getType()) +  market.getCardsGrid().get(i-3).peek().getCost() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-6).getType()) +  market.getCardsGrid().get(i).peek().getCost() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-9).getType()) +  market.getCardsGrid().get(i-9).peek().getCost() +"\u001B[0m" + "               ");
        output.println(paintCard(market.getCardsGrid().get(i).getType()) +  market.getCardsGrid().get(i).peek().getProductionPower() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-3).getType()) +  market.getCardsGrid().get(i-3).peek().getProductionPower() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-6).getType()) +  market.getCardsGrid().get(i).peek().getProductionPower() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-9).getType()) +  market.getCardsGrid().get(i-9).peek().getProductionPower() +"\u001B[0m" + "               ");
        output.println(paintCard(market.getCardsGrid().get(i).getType()) +  market.getCardsGrid().get(i).peek().getScore() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-3).getType()) +  market.getCardsGrid().get(i-3).peek().getScore() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-6).getType()) +  market.getCardsGrid().get(i).peek().getScore() +"\u001B[0m" + "               " + paintCard(market.getCardsGrid().get(i-9).getType()) +  market.getCardsGrid().get(i-9).peek().getScore() +"\u001B[0m" + "               ");
    }

    private static String paintCard(CardType cardType){
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
}
