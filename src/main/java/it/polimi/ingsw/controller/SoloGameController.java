package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.PopesFavorTileState;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.model.shared.SoloActionType;
import it.polimi.ingsw.server.Room;
import it.polimi.ingsw.utils.GameUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller for solo game (both online and local), it overrides some methods of ClassicGameController
 */
public class SoloGameController extends ClassicGameController {
    private final Game game;

    /**
     * SoloGameController constructor with room as parameter.
     */
    public SoloGameController(Room room) {
        super(room);
        game=super.getGame();
        game.setLorenzoIlMagnifico(new Player("LorenzoIlMagnifico"));
        game.setSoloActionTypes();
    }

    /**
     * SoloGameController constructor with room as parameter.
     */
    public SoloGameController(Game game) {
        super(game);
        this.game = game;
        game.setLorenzoIlMagnifico(new Player("LorenzoIlMagnifico"));
        game.setSoloActionTypes();
    }

    /**
     * Override the super method checking if LorenzoIlMagnifico won
     * @return true if the game is over
     */
    @Override
    public boolean isGameOver() {
        for (CardType cardType:
                CardType.values()) {
            if(game.getMarket().getCardsGrid().stream().filter(developmentCards -> (cardType.name().equals(developmentCards.getType().name()))&&(!developmentCards.isEmpty())).count() == 0)return true;
        }
        if(game.getLorenzoIlMagnifico().getFaithTrack().getPosition() >= FaithTrack.getMaxposition())return true;

        return super.isGameOver();
    }

    /**
     * Override the super method computing LorenzoIlMagnifico VP
     * @return the final standing standing
     */
    @Override
    public Map<String, Integer> computeStanding() {
        Map<String, Integer> standing = new HashMap<>();
        if(computeWinner().equals(game.getLorenzoIlMagnifico().getUsername())){
            standing.put(game.getLorenzoIlMagnifico().getUsername(), game.getLorenzoIlMagnifico().getVP());
            standing.put(game.getCurrentPlayer().getUsername(), game.getCurrentPlayer().getVP());
        }
        else {
            standing.put(game.getCurrentPlayer().getUsername(), game.getCurrentPlayer().getVP());
            standing.put(game.getLorenzoIlMagnifico().getUsername(), game.getLorenzoIlMagnifico().getVP());
        }
        return standing;
    }

    /**
     * Override the super method adding the check on LorenzoIlMagnifico
     * @return the name of the winner
     */
    @Override
    public String computeWinner() {
        if(game.getLorenzoIlMagnifico().getFaithTrack().getPosition()==FaithTrack.getMaxposition())return game.getLorenzoIlMagnifico().getUsername();
        for (CardType cardType:
                CardType.values()) {
            if(game.getMarket().getCardsGrid().stream().filter(developmentCards -> (cardType.name().equals(developmentCards.getType().name()))&&(!developmentCards.isEmpty())).count() == 0)return game.getLorenzoIlMagnifico().getUsername();
        }
        return super.computeWinner();
    }

    /**
     * Overrides super method by drawing LorenzoIlMagnifico action
     */
    @Override
    public void computeNextPlayer() {
        SoloActionType soloActionType = game.getSoloActionTypes().pop();
        switch (soloActionType){
            case PLUS_ONE:
                movePlayer("lorenzoilmagnifico", 1);
                game.setSoloActionTypes();
                break;
            case PLUS_TWO:
              movePlayer("lorenzoilmagnifico", 2);
                break;
            default:
                game.removeCardsByLorenzo(soloActionType);
                break;
        }
    }

    /**
     * Ovverides super method checking if Lorenzo have to activate Pope report or setting his tile to active/discarded if another player activated Pope Report
     */
    @Override
    public void activatePopeReport() {
        super.activatePopeReport();
        FaithTrack currentPlayerFaithTrack = game.getCurrentPlayer().getFaithTrack();
        if(currentPlayerFaithTrack.inOnPopeSpace()!= -1){
            if(game.getLorenzoIlMagnifico().getFaithTrack().isInPopeFavorByLevel(currentPlayerFaithTrack.inOnPopeSpace())){
                game.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(currentPlayerFaithTrack.inOnPopeSpace()-1).setState(PopesFavorTileState.ACTIVE);
            } else  game.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(currentPlayerFaithTrack.inOnPopeSpace()-1).setState(PopesFavorTileState.DISCARDED);
        }

        if(game.getLorenzoIlMagnifico().getFaithTrack().inOnPopeSpace()!= -1 && game.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(game.getLorenzoIlMagnifico().getFaithTrack().inOnPopeSpace()-1).getState().equals(PopesFavorTileState.INACTIVE)){
                game.getLorenzoIlMagnifico().getFaithTrack().getPopesFavorTiles().get(game.getLorenzoIlMagnifico().getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.ACTIVE);
            if(currentPlayerFaithTrack.isInPopeFavorByLevel(game.getLorenzoIlMagnifico().getFaithTrack().inOnPopeSpace())){
                currentPlayerFaithTrack.getPopesFavorTiles().get(game.getLorenzoIlMagnifico().getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.ACTIVE);
            } else  currentPlayerFaithTrack.getPopesFavorTiles().get(game.getLorenzoIlMagnifico().getFaithTrack().inOnPopeSpace()-1).setState(PopesFavorTileState.DISCARDED);
        }
    }

    /**
     * Ovverides wuper method by moving LorenzoIlMagnifico when resources are dropped
     * @param obtainedResources all the resources the player got shifting the marker
     * @param resourcesToDrop the resources the player does not wish to retain
     * @param playerUsername tha player tha selected the resources
     */
    @Override
    public void dropPlayerResources(Map<Resource, Integer> obtainedResources, Map<Resource, Integer> resourcesToDrop, String playerUsername) throws InvalidParameterException {
        super.dropPlayerResources(obtainedResources, resourcesToDrop, playerUsername);
        int totalDropped = 0;
        for (Resource r : resourcesToDrop.keySet()) {
            totalDropped += resourcesToDrop.get(r);
        }
        movePlayer(game.getLorenzoIlMagnifico().getUsername(), totalDropped);
    }
}
