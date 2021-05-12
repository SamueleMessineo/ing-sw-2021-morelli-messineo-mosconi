package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.utils.GameUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WarehouseTest {

    private Warehouse warehouse = new Warehouse();

    @Test
    public void getShelfIndex() {
        assertEquals(0, warehouse.getShelfIndex("top"));
        assertEquals(1, warehouse.getShelfIndex("middle"));
        assertEquals(2, warehouse.getShelfIndex("bottom"));
    }

    @Test
    public void getShelf() {
        assertEquals(warehouse.getShelves().get(0), warehouse.getShelf("top"));
        assertEquals(warehouse.getShelves().get(1), warehouse.getShelf("middle"));
        assertEquals(warehouse.getShelves().get(2), warehouse.getShelf("bottom"));
    }

    @Test
    public void getShelfResources() {
        assertEquals(warehouse.getShelves().get(0).getResources(), warehouse.getShelfResources("top"));
        assertEquals(warehouse.getShelves().get(1).getResources(), warehouse.getShelfResources("middle"));
        assertEquals(warehouse.getShelves().get(2).getResources(), warehouse.getShelfResources("bottom"));
    }

    @Test
    public void canPlaceOnShelf() {
        Map<Resource, Integer> resources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        assertFalse(warehouse.canPlaceOnShelf("top", resources));
        assertTrue(warehouse.canPlaceOnShelf("middle", resources));
        assertTrue(warehouse.canPlaceOnShelf("bottom", resources));
        resources.put(Resource.COIN, 3);
        assertFalse(warehouse.canPlaceOnShelf("middle", resources));
    }

    @Test
    public void placeOnShelf() {
        Map<Resource, Integer> resources = new HashMap<>(){{
            put(Resource.COIN, 0);
            put(Resource.STONE, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }};
        assertEquals(resources, warehouse.getResources());

        Map<Resource, Integer> newResources = new HashMap<>(){{
            put(Resource.STONE, 2);
        }};
        resources.put(Resource.STONE, 2);
        warehouse.placeOnShelf("middle", newResources);
        assertEquals(resources, warehouse.getResources());
        assertEquals(resources, warehouse.getShelfResources("middle"));
    }

    @Test
    public void switchShelves() {
        Map<Resource, Integer> firstResources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        Map<Resource, Integer> secondResources = new HashMap<>(){{
            put(Resource.STONE, 2);
        }};
        warehouse.placeOnShelf("middle", firstResources);
        warehouse.placeOnShelf("bottom", secondResources);
        warehouse.switchShelves("middle", "bottom");
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 2);
            put(Resource.COIN, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("middle"));
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 0);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("bottom"));
    }

    @Test
    public void switchShelves_oneEmpty() {
        Map<Resource, Integer> firstResources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        warehouse.placeOnShelf("middle", firstResources);
        warehouse.switchShelves("middle", "bottom");
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 0);
            put(Resource.COIN, 0);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("middle"));
        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 0);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getShelfResources("bottom"));
    }

    @Test
    public void getResources() {
        Map<Resource, Integer> firstResources = new HashMap<>(){{
            put(Resource.COIN, 2);
        }};
        Map<Resource, Integer> secondResources = new HashMap<>(){{
            put(Resource.STONE, 2);
        }};
        warehouse.placeOnShelf("middle", firstResources);
        warehouse.placeOnShelf("bottom", secondResources);

        assertEquals(new HashMap<Resource, Integer>(){{
            put(Resource.STONE, 2);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 0);
            put(Resource.SERVANT, 0);
        }}, warehouse.getResources());
    }

    @Test
    public void canPlaceResources(){
        Map<Resource,Integer> resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,7);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,8);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,3);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,1);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,0);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,0);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,0);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,0);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,0);
        assertTrue(warehouse.canPlaceResources(resToAdd));

//        Test for Warehouse with 1 extra shelf
        warehouse.addNewShelf("extra1",new Shelf(2,Resource.SERVANT));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,9);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,5);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,0);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,6);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,1);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,0);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,1);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,1);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        //        Test for Warehouse with 2 extra shelf
        warehouse.addNewShelf("extra2",new Shelf(2,Resource.COIN));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,11);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,5);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,1);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,3);
        resToAdd.put(Resource.STONE,3);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,5);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,6);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,5);
        resToAdd.put(Resource.COIN,5);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,2);
        assertFalse(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,5);
        resToAdd.put(Resource.COIN,4);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,0);
        assertTrue(warehouse.canPlaceResources(resToAdd));

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,3);
        resToAdd.put(Resource.STONE,2);
        resToAdd.put(Resource.SHIELD,2);
        assertTrue(warehouse.canPlaceResources(resToAdd));

    }
    @Test
    public void placeResources(){
        Map<Resource,Integer> resToAdd;
        Map<Resource,Integer> oldResInWarehouse;
        Map<Resource,Integer> mapToClear;


        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,1);
        oldResInWarehouse.put(Resource.STONE,1);
        oldResInWarehouse.put(Resource.SHIELD,0);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);

        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        oldResInWarehouse=new HashMap<>(warehouse.getResources());
        resToAdd.put(Resource.SERVANT,0);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,0);

        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

//      clear warehouse
        warehouse=new Warehouse();

        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,0);
        oldResInWarehouse.put(Resource.STONE,1);
        oldResInWarehouse.put(Resource.SHIELD,0);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);

        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        oldResInWarehouse=new HashMap<>(warehouse.getResources());
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,3);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);

        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

//        clear warehouse
        warehouse=new Warehouse();

        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,0);
        oldResInWarehouse.put(Resource.STONE,1);
        oldResInWarehouse.put(Resource.SHIELD,0);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,3);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,1);

        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

//        clear warehouse
        warehouse=new Warehouse();
        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,0);
        oldResInWarehouse.put(Resource.STONE,1);
        oldResInWarehouse.put(Resource.SHIELD,0);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,1);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,0);

        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

//      Test with extra1
        warehouse=new Warehouse();
        warehouse.addNewShelf("extra1",new Shelf(2,Resource.COIN));
        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,0);
        oldResInWarehouse.put(Resource.STONE,1);
        oldResInWarehouse.put(Resource.SHIELD,0);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,2);
        resToAdd.put(Resource.COIN,2);
        resToAdd.put(Resource.STONE,1);
        resToAdd.put(Resource.SHIELD,1);
        warehouse.placeResources(resToAdd);

        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

        warehouse=new Warehouse();
        warehouse.addNewShelf("extra1",new Shelf(2,Resource.COIN));
        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,1);
        oldResInWarehouse.put(Resource.COIN,0);
        oldResInWarehouse.put(Resource.STONE,0);
        oldResInWarehouse.put(Resource.SHIELD,2);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,0);
        resToAdd.put(Resource.COIN,3);
        resToAdd.put(Resource.STONE,0);
        resToAdd.put(Resource.SHIELD,0);
        warehouse.placeResources(resToAdd);

        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());

        warehouse=new Warehouse();
        warehouse.addNewShelf("extra1",new Shelf(2,Resource.STONE));
        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,0);
        oldResInWarehouse.put(Resource.STONE,0);
        oldResInWarehouse.put(Resource.SHIELD,2);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse,warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,1);
        resToAdd.put(Resource.COIN,0);
        resToAdd.put(Resource.STONE,5);
        resToAdd.put(Resource.SHIELD,0);
        warehouse.placeResources(resToAdd);

        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());


        warehouse=new Warehouse();
        warehouse.addNewShelf("extra1",new Shelf(2,Resource.STONE));
        warehouse.addNewShelf("extra2",new Shelf(2,Resource.COIN));

        oldResInWarehouse= GameUtils.emptyResourceMap();
        oldResInWarehouse.put(Resource.SERVANT,0);
        oldResInWarehouse.put(Resource.COIN,1);
        oldResInWarehouse.put(Resource.STONE,0);
        oldResInWarehouse.put(Resource.SHIELD,0);
        warehouse.placeResources(oldResInWarehouse);
        assertEquals(oldResInWarehouse, warehouse.getResources());

        resToAdd= GameUtils.emptyResourceMap();
        resToAdd.put(Resource.SERVANT,0);
        resToAdd.put(Resource.COIN,3);
        resToAdd.put(Resource.STONE,5);
        resToAdd.put(Resource.SHIELD,0);
        warehouse.placeResources(resToAdd);
        assertEquals(GameUtils.sumResourcesMaps(oldResInWarehouse,resToAdd),warehouse.getResources());
    }
}