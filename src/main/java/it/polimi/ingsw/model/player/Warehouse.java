package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;



public class Warehouse {

    private Shelf topShelf;
    private Shelf middleShelf;
    private Shelf bottomShelf;



    public Warehouse() {
        this.topShelf = new Shelf(1);
        this.middleShelf = new Shelf(2);
        this.bottomShelf = new Shelf(3);
    }

    //@ensures if (shelf == "top" => result.size <= 1) else
    //@ if (shelf == "middle" => result.size <= 2) else
    //@ if (shelf == "bottom" => result.size <= 3)
    public Shelf getShelf(String shelf){
        if (shelf.toLowerCase().trim().equals("top")) return topShelf;
        else if (shelf.toLowerCase().trim().equals("middle")) return middleShelf;
        else if (shelf.toLowerCase().trim().equals("bottom")) return bottomShelf;
        else return null;
    }

    public ArrayList<Resource> getResources(String shelf){
            if (shelf.toLowerCase().trim().equals("top")) return topShelf.getShelf();
            else if (shelf.toLowerCase().trim().equals("middle")) return middleShelf.getShelf();
            else if (shelf.toLowerCase().trim().equals("bottom")) return bottomShelf.getShelf();
            else return null;
        }


    //@requires resources.size <= 3 && forAll (Resource i in resources;;i=resources.get(0));
    public boolean canPlaceOnShelf(String shelf, ArrayList<Resource> resources){
        if (resources.size() > getShelf(shelf).getMaxSize()-getShelf(shelf).getShelf().size()) return false; //there is not enough empty space
        else {
            if (getShelf(shelf).getShelf().isEmpty())return true; //enough empty space and shelf has no type yet
            else if (getShelf(shelf).getShelf().get(0)!=resources.get(0))return false; //wrong resource type
            else return true; //right resource type
        }
        //TODO controllare che non ci siano le stesse risorse già in un altro shelf
    }

    public void placeOnShelf(String shelf, ArrayList<Resource> resources){
        if (canPlaceOnShelf(shelf, resources)) {
            for (Resource r :
                    resources) {
                getShelf(shelf).addResource(r);
            }
        }

    }

    public void switchShelves(String firstShelf, String secondShelf){
        if (getShelf(firstShelf).getShelf().size() <= getShelf(secondShelf).getMaxSize() && getShelf(secondShelf).getShelf().size() <= getShelf(firstShelf).getMaxSize()){

            Resource rfirst;
            rfirst = getShelf(firstShelf).getShelf().get(0);
            int firstSize = getShelf(firstShelf).getMaxSize();

            Resource rsecond;
            rsecond = getShelf(secondShelf).getShelf().get(0);
            int secondSize = getShelf(secondShelf).getMaxSize();

            for (int i = 0; i<firstSize;i++){
                getShelf(secondShelf).addResource(rfirst);
            }
            for (int i =0; i<secondSize; i++){
                getShelf(firstShelf).addResource(rsecond);
            }
        }

    }
    public void useResources(ArrayList<Resource> resources){
        for (Resource r :
             resources) {
            if (topShelf.getShelf().get(0) == resources.get(0))topShelf.getShelf().remove(0);
            else  if (middleShelf.getShelf().get(0) == resources.get(0))middleShelf.getShelf().remove(0);
            else  if (bottomShelf.getShelf().get(0) == resources.get(0))bottomShelf.getShelf().remove(0);
            else System.out.println("risorse non disponibili");
        }




    }

    public ArrayList<Resource> getResources(){
        ArrayList<Resource> result = new ArrayList<
                Resource>();
        result.addAll(topShelf.getShelf());
        result.addAll(middleShelf.getShelf());
        result.addAll(bottomShelf.getShelf());
        return result;
    }
}
