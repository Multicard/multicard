package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.services.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GameResetService {

    private final GameServiceImpl gameService;
    private final HandServiceImpl handService;
    private final CardServiceImpl cardService;
    private final ActionServiceImpl actionService;
    private final PLayedCardsServiceImpl playedCardsService;
    private final StackServiceImpl stackService;

    public GameResetService(GameServiceImpl gameService, HandServiceImpl handService, CardServiceImpl cardService, ActionServiceImpl actionService, PLayedCardsServiceImpl playedCardsService, StackServiceImpl stackService) {
        this.gameService = gameService;
        this.handService = handService;
        this.cardService = cardService;
        this.actionService = actionService;
        this.playedCardsService = playedCardsService;
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
        Set<Action> actionsToDelete = new HashSet<>(game.getActions());
        game.getActions().removeAll(game.getActions());
        for (ch.cas.html5.multicardgame.entity.Action action : actionsToDelete){
            action.getPlayer().getActions().remove(action);
            actionService.deleteAction(action.getId());
        }


        if (game.getPlayedcards() != null){

            Set<Card> playedCardsToDelete = new HashSet<>(game.getPlayedcards().getPlayedcards());

            for (Player player : game.getPlayers()){
                Set<Card> playerCards = new HashSet<>(player.getPlayedCards());
                player.getPlayedCards().removeAll(player.getPlayedCards());
                for (Card played : playerCards){
                    played.getPlayer().getPlayedCards().remove(played);
                    cardService.deleteCard(played);
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
        }
        System.out.println("reset finished");

    }


}
