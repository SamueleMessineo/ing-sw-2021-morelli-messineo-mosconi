package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleStructure;
import it.polimi.ingsw.model.shared.DevelopmentCard;
import it.polimi.ingsw.model.shared.LeaderCard;
import it.polimi.ingsw.model.shared.ProductionPower;
import it.polimi.ingsw.model.shared.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UI {

    void run();

    void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId);

    void setup();

    void displayError(String body);

    void displayString(String body);

    void selectInitialResources(List<Resource> resources, int amount);

    void selectLeaderCards(ArrayList<LeaderCard> leaderCards);

    void displayGameState();

    void displayPossibleMoves(List<String> moves);

    void setGameState(Game game);

    void selectMarbles(MarbleStructure marbleStructure);

    void dropResources(Map<Resource, Integer> resources);

    void selectResourceForWhiteMarbles(int amount, List<Resource> options);

    void discardLeaderCard(ArrayList<LeaderCard> cards);

    void switchShelves(ArrayList<String> shelves);

    void activateProduction(List<ProductionPower> productionPowers);

    void buyDevelopmentCard(List<DevelopmentCard> developmentCards);

    void selectStackToPlaceCard(List<Integer> stacks);

    void playLeader(List<LeaderCard> leaderCards);

    void gameOver(String winner, Map<String, Integer> standing);

    void askUsername();

    String getUsername();
}
