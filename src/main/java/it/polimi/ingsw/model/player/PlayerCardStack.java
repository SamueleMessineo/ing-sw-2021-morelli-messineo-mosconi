package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.io.Serializable;
import java.util.Stack;

/**
 * The stack of development cards bought by the player during the game
 * and placed on his board.
 */
public class PlayerCardStack extends Stack<DevelopmentCard> implements Serializable {

    /**
     * Checks if a new card can be placed on the stack.
     * @param card The new card.
     * @return True if the card can be placed, false if not.
     */
    public boolean canPlaceCard(DevelopmentCard card){
        if (this.isEmpty())
            return card.getLevel()==1;
        return card.getLevel() == this.peek().getLevel() + 1;
    }

    /**
     * Places the given Development card on the top of the stack.
     * @param card The new card.
     * @return The card that has been placed.
     */
    @Override
    public DevelopmentCard push(DevelopmentCard card) {
        if(canPlaceCard(card))
            return super.push(card);
        return null;
    }

    @Override
    public synchronized String toString() {
        String result = "";
        if(size() == 0)return "empty";
        result+=(peek().toString());
        for (int i = size()-2; i >= 0; i--) {
            result += "\nlevel card-"+i+": "+(get(i).getLevel());
        }
        return result;
    }
}
