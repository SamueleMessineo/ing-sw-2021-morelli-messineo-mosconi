package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.network.ClientMessageHandler;

import java.util.ArrayList;

/**
 * Message with the productionPowers the player can activate.
 */
public class ActivateProductionRequestMessage extends ClientMessage {
    private final ArrayList<ProductionPower> productionPowers;

    public ActivateProductionRequestMessage(ArrayList<ProductionPower> productionPowers) {
        this.productionPowers = productionPowers;
    }

    public ArrayList<ProductionPower> getProductionPowers() {
        return productionPowers;
    }

    public void accept (ClientMessageHandler handler){
        handler.handle(this);
    }
}
