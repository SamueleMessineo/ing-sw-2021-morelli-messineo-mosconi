package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GameUtilsTest {
    @Test
    public void emptyResourceMap() {
        Map<Resource, Integer> emptyMap=new HashMap<>();
        emptyMap.put(Resource.STONE,0);
        emptyMap.put(Resource.COIN,0);
        emptyMap.put(Resource.SHIELD,0);
        emptyMap.put(Resource.SERVANT,0);

        assertEquals(emptyMap,GameUtils.emptyResourceMap());
    }

    @Test
    public void sumResourcesMaps() {
        Map<Resource, Integer> map1=new HashMap<>();
        map1.put(Resource.STONE,4);
        map1.put(Resource.COIN,5);
        map1.put(Resource.SHIELD,6);
        map1.put(Resource.SERVANT,7);

        Map<Resource, Integer> map2=new HashMap<>();
        map2.put(Resource.SHIELD,6);
        map2.put(Resource.SERVANT,7);

        Map<Resource, Integer> sum=new HashMap<>();
        sum.put(Resource.STONE,4);
        sum.put(Resource.COIN,5);
        sum.put(Resource.SHIELD,12);
        sum.put(Resource.SERVANT,14);

        assertEquals(sum,GameUtils.sumResourcesMaps(map1,map2));


        map1=new HashMap<>();
        map1.put(Resource.STONE,4);
        map1.put(Resource.COIN,5);

        map2=new HashMap<>();
        map2.put(Resource.SHIELD,1);
        map2.put(Resource.SERVANT,9);

        sum=new HashMap<>();
        sum.put(Resource.STONE,4);
        sum.put(Resource.COIN,5);
        sum.put(Resource.SHIELD,1);
        sum.put(Resource.SERVANT,9);

        assertEquals(sum,GameUtils.sumResourcesMaps(map1,map2));

        map1=new HashMap<>();
        map1.put(Resource.STONE,21);
        map1.put(Resource.COIN,12);

        map2=new HashMap<>();
        map2.put(Resource.STONE,6);
        map2.put(Resource.COIN,7);
        map2.put(Resource.SHIELD,1);
        map2.put(Resource.SERVANT,9);

        sum=new HashMap<>();
        sum.put(Resource.STONE,27);
        sum.put(Resource.COIN,19);
        sum.put(Resource.SHIELD,1);
        sum.put(Resource.SERVANT,9);

        assertEquals(sum,GameUtils.sumResourcesMaps(map1,map2));


    }
}