package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.List;

public class SelectStackToPlaceCardRequestMessage extends ClientMessage {
    private final List<DevelopmentCard> developmentCards;

    public SelectStackToPlaceCardRequestMessage(List<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public void accept(ClientMessageHandler handler) {
        handler.handle(this);
    }

}
