package it.polimi.ingsw.model.shared;

import java.io.Serializable;

/**
 * Defines the possible card types
 */
public enum CardType implements Serializable {
    GREEN, YELLOW, PURPLE, BLUE;

    @Override
    public String toString() {
        switch (this){
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
}
