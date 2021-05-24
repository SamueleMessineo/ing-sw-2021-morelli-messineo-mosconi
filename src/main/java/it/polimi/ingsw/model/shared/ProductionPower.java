package it.polimi.ingsw.model.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The productionPower class represents the production power of the Development Card
 */

public class ProductionPower implements Serializable {
    private final Map<Resource, Integer> input;
    private final Map<Resource, Integer> output;

    public ProductionPower(Map<Resource, Integer> input, Map<Resource, Integer> output){
        this.input=input;
        this.output=output;
    }



    /**
     * Fetch the resources to pay to activate the production power
     * @return A list of the Resources
     */
    public Map<Resource, Integer> getInput(){
        return input;
    }

    /**
     * Fetch the resources received from the production power
     * @return A list of the Resources
     */
    public Map<Resource, Integer> getOutput(){
        return output;
    }

    @Override
    public String toString() {
        return "power{" +
                input + "-->" +
                 output +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        ProductionPower other = (ProductionPower)obj;
        return input.equals(other.input)&&output.equals(other.output);
    }
}
