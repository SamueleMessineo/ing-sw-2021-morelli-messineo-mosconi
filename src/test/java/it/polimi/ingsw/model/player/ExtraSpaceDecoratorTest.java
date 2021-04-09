package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExtraSpaceDecoratorTest {

    Warehouse warehouse = new Warehouse();

    @Test
    public void firstDecoration() {
        warehouse.addNewShelf("extra" + (warehouse.getShelfNames().size()-2),
                new Shelf(2, Resource.COIN));
        ArrayList<String> names = new ArrayList<>(){{
            add("top");
            add("middle");
            add("bottom");
            add("extra1");
        }};
        assertEquals(4, warehouse.getShelves().size());
        assertEquals(names, warehouse.getShelfNames());
        assertEquals(Resource.COIN, warehouse.getShelf("extra1").getResourceType());
    }

    @Test
    public void doubleDecoration() {
        warehouse.addNewShelf("extra" + (warehouse.getShelfNames().size()-2),
                new Shelf(2, Resource.COIN));
        warehouse.addNewShelf("extra" + (warehouse.getShelfNames().size()-2),
                new Shelf(2, Resource.SHIELD));
        ArrayList<String> names = new ArrayList<>(){{
            add("top");
            add("middle");
            add("bottom");
            add("extra1");
            add("extra2");
        }};
        assertEquals(5, warehouse.getShelves().size());
        assertEquals(names, warehouse.getShelfNames());
        assertEquals(Resource.COIN, warehouse.getShelf("extra1").getResourceType());
        assertEquals(Resource.SHIELD, warehouse.getShelf("extra2").getResourceType());
    }
}