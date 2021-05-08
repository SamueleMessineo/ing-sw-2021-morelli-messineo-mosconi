package it.polimi.ingsw.network.game;

import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.network.GameMessageHandler;

import java.util.List;

public class ActivateProductionResponseMessage extends GameMessage {
    private final List<Integer> selectedStacks;
    private final ProductionPower basicProduction;
    private final List<Integer> extraProductionPowers;

    public ActivateProductionResponseMessage(List<Integer> selectedStacks, ProductionPower basicProduction, List<Integer> extraProductionPowers) {
        this.selectedStacks = selectedStacks;
        this.basicProduction = basicProduction;
        this.extraProductionPowers = extraProductionPowers;
    }

    public List<Integer> getSelectedStacks() {
        return selectedStacks;
    }

    public ProductionPower getBasicProduction() {
        return basicProduction;
    }

    public List<Integer> getExtraProductionPowers() {
        return extraProductionPowers;
    }

    public void accept(GameMessageHandler handler) {
        handler.handle(this);
    }
}
