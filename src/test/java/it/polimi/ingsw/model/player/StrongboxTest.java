package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class StrongboxTest {

    Strongbox strongbox;

    @Before
    public void setUp() throws Exception {
            this.strongbox=new Strongbox();
    }

    @Test
    public void addResources() {
        Map<Resource, Integer> add_res=new HashMap<>();
        add_res.put(Resource.SERVANT, 12);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 9);
        add_res.put(Resource.SHIELD, 15);

        strongbox.addResources(add_res);
        assertEquals(add_res, strongbox.getResources());

        add_res.put(Resource.SERVANT, 3);
        add_res.put(Resource.COIN, 6);
        add_res.put(Resource.STONE, 12);
        add_res.put(Resource.SHIELD, 9);

        strongbox.addResources(add_res);

        add_res.put(Resource.SERVANT, 15);
        add_res.put(Resource.COIN, 12);
        add_res.put(Resource.STONE, 21);
        add_res.put(Resource.SHIELD, 24);
        assertEquals(add_res, strongbox.getResources());

    }

    @Test
    public void useResources() {
        Map<Resource, Integer> sub_res=new HashMap<>();
        Map<Resource, Integer> add_res=new HashMap<>();
        add_res.put(Resource.SERVANT, 21);
        add_res.put(Resource.COIN, 18);
        add_res.put(Resource.STONE, 23);
        add_res.put(Resource.SHIELD, 15);

        strongbox.addResources(add_res);

        sub_res.put(Resource.SERVANT, 10);
        sub_res.put(Resource.COIN, 12);
        sub_res.put(Resource.STONE, 17);
        sub_res.put(Resource.SHIELD, 6);

        strongbox.useResources(sub_res);

        sub_res.put(Resource.SERVANT, 11);
        sub_res.put(Resource.COIN, 6);
        sub_res.put(Resource.STONE, 6);
        sub_res.put(Resource.SHIELD, 9);

        assertEquals(sub_res,strongbox.getResources());

    }
}