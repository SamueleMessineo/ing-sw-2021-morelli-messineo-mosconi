package it.polimi.ingsw.view;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.Resource;

import java.io.PrintStream;

public class Display {


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


        for (int i = 11; i >= 0; i-=3) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else {
                output.println(market.getCardsGrid().get(i));
                output.println("\n");
            }

        }
        for (int i = 10; i >= 0; i-=3) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else {
                output.println(market.getCardsGrid().get(i));
                output.println("\n");
            }
        }
        for (int i = 9; i >= 0; i-=3) {
            if(market.getCardsGrid().get(i).isEmpty()) System.out.println("Empty Stack");
            else {
                output.println(market.getCardsGrid().get(i));
                output.println("\n");
            }
        }

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
    }
}
