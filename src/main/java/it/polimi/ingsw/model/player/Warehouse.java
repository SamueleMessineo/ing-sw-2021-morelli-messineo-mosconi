package it.polimi.ingsw.model.player;
import it.polimi.ingsw.model.shared.*;

import java.util.ArrayList;
import java.util.Arrays;


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
    public ArrayList<Resource> getShelf(String shelf){
        if (shelf.toLowerCase().trim().equals("top")) return topShelf.getShelf();
        else if (shelf.toLowerCase().trim().equals("middle")) return middleShelf.getShelf();
        else if (shelf.toLowerCase().trim().equals("bottom")) return bottomShelf.getShelf();
        else return null;
    }
    //@requires resources.size <= 3 && forAll (Resource i in resources;;i=resources.get(0));
    public boolean canPlaceOnShelf(Shelf shelf, ArrayList<Resource> resources){
        if (resources.size() > shelf.getMaxSize()-shelf.getShelf().size()) return false; //there is not enough empty space
        else {
            if (shelf.getShelf().isEmpty())return true; //enough empty space and shelf has no type yet
            else if (shelf.getShelf().get(0)!=resources.get(0))return false; //wrong resource type
            else return true; //right resource type
        }

    }

    public void PlaceOnShelf(Shelf shelf, ArrayList<Resource> resources){
        if (canPlaceOnShelf(shelf, resources)) {
            for (Resource r :
                    resources) {
                shelf.addResource(r);
            }
        }

    };

    public void switchShelves(Shelf firstShelf, Shelf secondShelf){
        if (canPlaceOnShelf(firstShelf, secondShelf.getShelf()) && canPlaceOnShelf(secondShelf, firstShelf.getShelf())){

            Resource rfirst;
            rfirst = firstShelf.getShelf().get(0);
            int firstSize = firstShelf.getMaxSize();

            Resource rsecond;
            rsecond = secondShelf.getShelf().get(0);
            int secondSize = secondShelf.getMaxSize();

            for (int i = 0; i<firstSize;i++){
                secondShelf.addResource(rfirst);
            }
            for (int i =0; i<secondSize; i++){
                firstShelf.addResource(rsecond);
            }
        }

    }
    public void useResources(ArrayList<Resource> resources){
        if (topShelf.getShelf().get(0) == resources.get(0))topShelf.getShelf().remove(0);
        else if (middleShelf.getShelf().get(0) == resources.get(0)){
            for (int i = 0; i < resources.size(); i++)middleShelf.getShelf().remove(i);
        }
        else if (bottomShelf.getShelf().get(0) == resources.get(0)){
            for (int i = 0; i < resources.size(); i++)bottomShelf.getShelf().remove(i);
        }

    };
    public ArrayList<Resource>getResources(){
        ArrayList<Resource> result = new ArrayList<Resource>();
        result.addAll(topShelf.getShelf());
        result.addAll(middleShelf.getShelf());
        result.addAll(bottomShelf.getShelf());
        return result;
    };
}
