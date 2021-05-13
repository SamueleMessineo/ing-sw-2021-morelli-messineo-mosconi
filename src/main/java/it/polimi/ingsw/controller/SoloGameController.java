package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.FaithTrack;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.shared.CardType;
import it.polimi.ingsw.model.shared.PopesFavorTileState;
import it.polimi.ingsw.model.shared.Resource;
import it.polimi.ingsw.model.shared.SoloActionType;
import it.polimi.ingsw.server.Room;

public class SoloGameController extends ClassicGameController {
    private final Game game;

    public SoloGameController(Room room) {
        super(room);
        game=super.getGame();
        game.setLorenzoIlMagnifico(new Player("LorenzoIlMagnifico"));
        game.setSoloActionTypes();
    }

    public SoloGameController(Game game) {
        super(game);
        this.game = game;
        game.setLorenzoIlMagnifico(new Player("LorenzoIlMagnifico"));
        game.setSoloActionTypes();
    }

    @Override
    public boolean isGameOver() {

        for (CardType cardType:
                CardType.values()) {
            if(game.getMarket().getCardsGrid().stream().filter(developmentCards -> (cardType.name().equals(developmentCards.getType().name()))&&(!developmentCards.isEmpty())).count() == 0)return true;
        }

        if(game.getLorenzoIlMagnifico().getFaithTrack().getPosition() == FaithTrack.getMaxposition())return true;


        return super.isGameOver();
    }

    @Override
    public void movePlayer(String playerName, int positions) {
        super.movePlayer(playerName, positions);
        tryPopeReport(game.getLorenzoIlMagnifico());
    }

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
}
