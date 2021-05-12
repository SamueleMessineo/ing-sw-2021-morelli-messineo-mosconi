package it.polimi.ingsw.model.shared;

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
                return "🟡";
            case FAITH:
                return "✝";
            case SERVANT:
                return "S🧞";
            case SHIELD:
                return "🛡";
            case STONE:
                return "🪨️";
            case ANY:
                return "🃏️";
        }
       return "";
    }
}
