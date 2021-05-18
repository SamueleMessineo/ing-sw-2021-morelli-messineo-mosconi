package it.polimi.ingsw.model.shared;

import it.polimi.ingsw.view.Display;

import java.io.Serializable;

/**
 * Defines the various types of resources.
 */
public enum Resource implements Serializable {
    STONE,SHIELD,COIN,SERVANT,FAITH,ANY;

    @Override
    public String toString() {
        switch (this){
            case COIN:
                return Display.paint("YELLOW", "⬤");
            case FAITH:
                return Display.paint("RED","✝");
            case SERVANT:
                return Display.paint("PURPLE", "♟");
            case SHIELD:
                return Display.paint("BLUE", "♦");
            case STONE:
                return  Display.paint("GREY", "☗");
            case ANY:
                return "🃏️";
        }
       return "";
    }


}
