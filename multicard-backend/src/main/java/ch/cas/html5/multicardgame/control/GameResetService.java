package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GameResetService {

    @Autowired
    private GameServiceImpl gameService;

    public void setGameServiceImpl(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @Autowired
    private PlayerServiceImpl playerService;

    public void setPlayerServiceImpl(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @Autowired
    private HandServiceImpl handService;

    public void setHandServiceImpl(HandServiceImpl handService) {
        this.handService = handService;
    }

    @Autowired
    private DeckServiceImpl deckService;

    public void setDeckService(DeckServiceImpl deckService) {
        this.deckService = deckService;
    }

    @Autowired
    private DeckelementServiceImpl deckelementService;

    public void setDeckelementService(DeckelementServiceImpl deckelementService) {
        this.deckelementService = deckelementService;
    }

    @Autowired
    private CardServiceImpl cardService;

    public void setCardService(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @Autowired
    private ActionServiceImpl actionService;

    public void setActionService(ActionServiceImpl actionService) {
        this.actionService = actionService;
    }

    @Autowired
    private PLayedCardsServiceImpl playedCardsService;

    public void setPlayedCardsService(PLayedCardsServiceImpl playedCardsService) {
        this.playedCardsService = playedCardsService;
    }

    @Autowired
    private StackServiceImpl stackService;

    public void setStackService(StackServiceImpl stackService) {
        this.stackService = stackService;
    }


    public void resetGame(Game game){
        System.out.println("reset started");
        game.setState(Gamestate.READYTOSTART);

        // delete Game.Stacks
        Set<Stack> stackList = game.getGameStacks();
        for (Stack stack : stackList) {
            game.getGameStacks().remove(stack);
            gameService.updateGame(game);
        }

        // delete Game.Actions
        Set<ch.cas.html5.multicardgame.entity.Action> actionsToDelete = new HashSet<>();
        actionsToDelete.addAll(game.getActions());
        game.getActions().removeAll(game.getActions());
        for (ch.cas.html5.multicardgame.entity.Action action : actionsToDelete){
            action.getPlayer().getActions().remove(action);
            actionService.deleteAction(action.getId());
        }


        if (game.getPlayedcards() != null){

            Set<Card> playedCardsToDelete = new HashSet<>();
            playedCardsToDelete.addAll(game.getPlayedcards().getPlayedcards());

            for (Player player : game.getPlayers()){
//                if (player.getPlayedCard() != null && player.getPlayedCard().getPlayedcards().getPlayedcards().size() > 0){
//                    player.getPlayedCard().getPlayedcards().getPlayedcards().removeAll(player.getPlayedCard().getPlayedcards().getPlayedcards());
//                }
                if (player.getPlayedCard() != null ){
                    player.setPlayedCard(null);
                }
            }

//            game.getPlayedcards().getPlayedcards().removeAll(game.getPlayedcards().getPlayedcards());
            for (Card playedCard : playedCardsToDelete){
                game.getPlayedcards().getPlayedcards().remove(playedCard);
                playedCard.setPlayedcards(null);
                playedCard.setPlayer(null);
                cardService.deleteCard(playedCard);
            }
            String id = game.getPlayedcards().getId();
            game.setPlayedcards(null);
            playedCardsService.deletePlayedCards(id);
        }


        for (Player player : game.getPlayers()) {

            // delete Player.Stacks
            for (Stack stack : player.getStacks()) {
                player.getStacks().remove(stack);
                stackService.deleteStack(stack.getId());
            }

            // delete Player.Hand
            Hand toDelete = player.getHand();
            if (toDelete != null) {
                player.setHand(null);
                handService.deleteHand(toDelete.getId());
            }

            player.setPlayerReady(false);
//            playerService.savePlayer(player);
        }
        System.out.println("reset finished");

    }


}
