package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;


public class ExtraSpaceDecorator extends Warehouse {
    private Warehouse warehouse;
    private Shelf extraShelf;
    private Resource type;

    public ExtraSpaceDecorator(Warehouse warehouse, Resource type) {
        this.warehouse = warehouse;
        this.type = type;
    }

    /**
     *
     * @param shelf
     * @return
     */
    @Override
    public Shelf getShelf(String shelf) {
        if (shelf.toLowerCase().trim().equals("top")) return warehouse.getShelf("top");
        else if (shelf.toLowerCase().trim().equals("middle")) return warehouse.getShelf("middle");
        else if (shelf.toLowerCase().trim().equals("bottom")) return warehouse.getShelf("bottom");
        else if (shelf.toLowerCase().trim().equals("extra")) return warehouse.getShelf("extra");
        else return null;
    }

    /**
     *
     * @param shelf
     * @return
     */
    @Override
    public ArrayList<Resource> getResources(String shelf) {
        if (shelf.toLowerCase().trim().equals("top")) return warehouse.getShelf("top").getShelf();
        else if (shelf.toLowerCase().trim().equals("middle")) return warehouse.getShelf("middle").getShelf();
        else if (shelf.toLowerCase().trim().equals("bottom")) return warehouse.getShelf("bottom").getShelf();
        else if (shelf.toLowerCase().trim().equals("extra")) return warehouse.getShelf("extra").getShelf();
        else return null;
    }

    /**
     *
     * @param shelf
     * @param resources
     * @return
     */
    @Override
    public boolean canPlaceOnShelf(String shelf, ArrayList<Resource> resources) {
        if (!shelf.toLowerCase().trim().equals("extra")) return super.canPlaceOnShelf(shelf, resources);
        else {
            for (Resource r :
                    resources) {
                if (r != type) return false;
            }
            if (extraShelf.getMaxSize() - extraShelf.getShelf().size() <= resources.size()) return true;
            else return false;
        }
    }

    //place on shelf non ci dovrebbe essere bisogno di ovveridarlo
    //idem get resources

    /**
     *
     * @param resources
     */
    @Override
    public void useResources(ArrayList<Resource> resources) {
        for (Resource r :
                resources) {
            if (warehouse.getShelf("shelf").getShelf().get(0) == resources.get(0))
                warehouse.getShelf("shelf").getShelf().remove(0);
            else if (warehouse.getShelf("middle").getShelf().get(0) == resources.get(0))
                warehouse.getShelf("middle").getShelf().remove(0);
            else if (warehouse.getShelf("bottom").getShelf().get(0) == resources.get(0))
                warehouse.getShelf("bottom").getShelf().remove(0);
            else if (warehouse.getShelf("extra").getShelf().get(0) == resources.get(0)) extraShelf.getShelf().remove(0);
            else System.out.println("risorse non disponibili");
        }
    }
}
