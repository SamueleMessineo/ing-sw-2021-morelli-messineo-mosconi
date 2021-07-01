package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

/**
 * Message with the developmentCards that the player can buy.
 */
public class BuyDevelopmentCardRequestMessage extends ClientMessage {
    private final List<DevelopmentCard> developmentCards;

    public BuyDevelopmentCardRequestMessage(List<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public void accept (ClientMessageHandler handler){
        handler.handle(this);
    }
}
