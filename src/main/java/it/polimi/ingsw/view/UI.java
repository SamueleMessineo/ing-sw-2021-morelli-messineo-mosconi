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

/**
 * The UI interface, CLI and GUI implements this interface.
 */
public interface UI {

    /**
     * Starter method, runs the application.
     */
    void run();

    /**
     * Displays the initial room details.
     * @param players the usernames of the players currently in the room.
     * @param playersNum the number of players required to fill the room.
     * @param RoomId the id of the room.
     */
    void displayRoomDetails(ArrayList<String> players, int playersNum, int RoomId);

    /**
     * Setups the UI.
     */
    void setup();

    /**
     * Displays an error message.
     * @param body the body of the error message.
     */
    void displayError(String body);

    /**
     * Displays a string message.
     * @param body the body of the message.
     */
    void displayString(String body);

    /**
     * Lets the user select their initial resources.
     * @param resources the list of possible resources.
     * @param amount the number of resources that need to be selected.
     */
    void selectInitialResources(List<Resource> resources, int amount);

    /**
     * Lets the user select their initial leader cards.
     * @param leaderCards the list of 4 leader cards to choose from.
     */
    void selectLeaderCards(ArrayList<LeaderCard> leaderCards);

    /**
     * Displays the game state.
     */
    void displayGameState();

    /**
     * Shows the moves the player is allowed to do.
     * @param moves the list of possible moves.
     */
    void displayPossibleMoves(List<String> moves);

    /**
     * Saves locally the game state.
     * @param game the game state.
     */
    void setGameState(Game game);

    /**
     * Allows the user to make a move on the marbles grid.
     * @param marbleStructure the marble structure.
     */
    void selectMarbles(MarbleStructure marbleStructure);

    /**
     * Asks the user to drop some of the obtained resources.
     * @param resources the resources obtained from the marble move.
     */
    void dropResources(Map<Resource, Integer> resources);

    /**
     * Asks the user how the white marbles should be converted, this
     * happens when the player has two leader cards active with their effect
     * on the marbles.
     * @param amount the number of white marbles to be converted.
     * @param options the resources the white marbles can be converted to.
     */
    void selectResourceForWhiteMarbles(int amount, List<Resource> options);

    /**
     * Lets the user discard one of his leader cards.
     * @param cards the player's leader cards.
     */
    void discardLeaderCard(ArrayList<LeaderCard> cards);

    /**
     * Lets the player switch the contents of two shelves.
     * @param shelves the player's shelves.
     */
    void switchShelves(ArrayList<String> shelves);

    /**
     * Asks the player which production powers to activate.
     * @param productionPowers the player's production powers.
     */
    void activateProduction(List<ProductionPower> productionPowers);

    /**
     * Asks the player which development card to buy.
     * @param developmentCards the cards that the player can buy and place.
     */
    void buyDevelopmentCard(List<DevelopmentCard> developmentCards);

    /**
     * Asks the player where to place a newly bought development card.
     * @param stacks the possible stacks the card could be placed on.
     */
    void selectStackToPlaceCard(List<Integer> stacks);

    /**
     * Lets the player play one of his leader cards.
     * @param leaderCards the player's leader cards that can be played.
     */
    void playLeader(List<LeaderCard> leaderCards);

    /**
     * Displays the game over info.
     * @param winner the username of the winner
     * @param standing the standings.
     */
    void gameOver(String winner, Map<String, Integer> standing);

    /**
     * Asks the user for a username.
     */
    void askUsername();

    /**
     * Retuns the player's username.
     * @return the player's username.
     */
    String getUsername();
}
