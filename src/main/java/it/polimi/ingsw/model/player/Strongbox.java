package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.HashMap;
import java.util.Map;

public class Strongbox implements Storage {

    private Map<Resource, Integer> resources;

    public Strongbox(){
        this.resources=new HashMap<Resource, Integer>();
        resources.put(Resource.COIN, 0);
        resources.put(Resource.SERVANT, 0);
        resources.put(Resource.STONE, 0);
        resources.put(Resource.SHIELD, 0);
    }

    public Map<Resource,Integer> getResources() { return resources; }

    @Override
    public void addResources(Map<Resource, Integer> resources) {

    }

    public void useResources(Map<Resource,Integer> res) {
        this.resources.forEach((resource,integer)->
                this.resources.put(resource,integer - res.get(resource)));
    }
}