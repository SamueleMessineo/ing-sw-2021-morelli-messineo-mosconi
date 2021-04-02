package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.util.Stack;

public class PlayerCardStack extends Stack<DevelopmentCard> {

    public boolean canPlaceCard(DevelopmentCard card){
        if (this.isEmpty())
            return card.getLevel()==1;
        return card.getLevel() == this.peek().getLevel() + 1;
    }

    @Override
    public DevelopmentCard push(DevelopmentCard item) {
        if(canPlaceCard(item))
            return super.push(item);
        return null;
    }
}
