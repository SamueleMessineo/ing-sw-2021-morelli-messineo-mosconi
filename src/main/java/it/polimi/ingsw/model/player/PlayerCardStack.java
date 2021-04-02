package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.DevelopmentCard;

import java.util.ArrayList;

public class PlayerCardStack {
    // TODO extends Stack class

    private ArrayList<DevelopmentCard> cards = new ArrayList<>();


    public DevelopmentCard getTopCard(){
        return cards.get(cards.size()-1);
    }

    public ArrayList<DevelopmentCard> getCards() {
        return cards;
    }

    public boolean canPlaceCard(DevelopmentCard card){
        if(cards.isEmpty())return true;
        else {
            if ((card.getLevel()>getTopCard().getLevel())&&(card.getCardType()==getTopCard().getCardType()))return true;
            else return false;
        }
    }

    public void placeCard(DevelopmentCard card){
        if (canPlaceCard(card)){
            cards.add(card);
        }

    }
}
