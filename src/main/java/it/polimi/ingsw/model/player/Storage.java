package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.Map;

public interface Storage {

    public Map<Resource, Integer> getResources();

    public void addResources(Map<Resource, Integer> resources);

    public int useResources(Map<Resource, Integer> resources);

}
