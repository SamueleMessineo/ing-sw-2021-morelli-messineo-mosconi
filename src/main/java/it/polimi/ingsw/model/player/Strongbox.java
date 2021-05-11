package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The player's strongbox.
 */
public class Strongbox implements Storage, Serializable {
    private Map<Resource, Integer> resources;

    public Strongbox(){
        this.resources=new HashMap<Resource, Integer>();
        resources.put(Resource.COIN, 0);
        resources.put(Resource.SERVANT, 0);
        resources.put(Resource.STONE, 0);
        resources.put(Resource.SHIELD, 0);
    }

    /**
     * Returns all the resources contained in the strongbox.
     * @return A map of all the resources in the strongbox.
     */
    public Map<Resource,Integer> getResources() { return new HashMap<>(resources); }

    /**
     * Adds the given resources to the strongbox.
     * @param res The map of resources to be added.
     */
    @Override
    public void addResources(Map<Resource, Integer> res) {
        this.resources.forEach((resource,integer)->
                this.resources.put(resource,integer + res.get(resource)));
    }

    /**
     * Removes the given resources from the strongbox.
     * @param res The map of resources to be removed.
     */
    public void useResources(Map<Resource,Integer> res) {
        this.resources.forEach((resource,integer)->
                this.resources.put(resource,integer - res.get(resource)));
    }

    @Override
    public String toString() {
        return "Coin: " + resources.get(Resource.COIN) + " Servant: " + resources.get(Resource.SERVANT) + " Stone: " + resources.get(Resource.STONE) + " Shield: " + resources.get(Resource.SHIELD);
    }

    public boolean canPayResources(Map<Resource, Integer>cost) {
        for (Resource resource : cost.keySet()) {
            if (resources.get(resource) < cost.get(resource)) return false;
        }
        return true;
    }
}