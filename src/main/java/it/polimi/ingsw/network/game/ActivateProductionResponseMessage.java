package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;

public class ActivateProductionResponseMessage extends GameMessage {
    private final List<Integer> selectedStacks;
    private final ProductionPower basicProduction;

    public ActivateProductionResponseMessage(List<Integer> selectedStacks, ProductionPower productionPower) {
        this.selectedStacks = selectedStacks;
        this.basicProduction = productionPower;
    }

    public List<Integer> getSelectedStacks() {
        return selectedStacks;
    }

    public ProductionPower getBasicProduction() {
        return basicProduction;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
