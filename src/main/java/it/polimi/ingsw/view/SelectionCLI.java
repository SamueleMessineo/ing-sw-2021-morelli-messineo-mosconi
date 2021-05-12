package it.polimi.ingsw.view;

import java.io.PrintStream;
import java.util.Scanner;

public class SelectionCLI {
    private final Scanner input;
    private final PrintStream output;

    public SelectionCLI() {
        input = new Scanner(System.in);
        output= new PrintStream(System.out);
    }

    public boolean playingOnLine(){
        Display.displayWelcomeMessage(output);
        do {
            output.println("Do you want to play online?[y/n]");
            String selection = input.nextLine().trim().toLowerCase();
            if(selection.startsWith("y"))return true;
            else if (selection.startsWith("n")) return false;
            else output.println("Selection not valid");
        }while (true);

    }
}
