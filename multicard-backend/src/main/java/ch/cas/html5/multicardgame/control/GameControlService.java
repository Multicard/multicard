package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.*;
import ch.cas.html5.multicardgame.entity.Stack;
import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.enums.Action;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.messages.GameMessage;
import ch.cas.html5.multicardgame.messages.GameStateMessage;
import ch.cas.html5.multicardgame.messages.PlayedCardMessage;
import ch.cas.html5.multicardgame.messages.RevertLastPlayerActionMessage;
import ch.cas.html5.multicardgame.messaging.WebSocketController;
import ch.cas.html5.multicardgame.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameControlService {
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

    @Autowired
    private GameResetService gameReset;

    public void setGameResetService(GameResetService gameReset) {
        this.gameReset = gameReset;
    }

    @Autowired
    private WebSocketController webController;

    public void setWebSocketController(WebSocketController webController) {
        this.webController = webController;
    }


    public void getGameAndSetReady(String gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            return;
        }
        setGameReady(game);
        convertAndPublishGame(game, null);
    }

    public void setGameReady(Game game) {

        System.out.println("Set Game Ready: " + game.getTitle());

        gameReset.resetGame(game);

        //ToDo - replace fix deck and cleanup stack
        String deckId = deckService.retrieveDecks().get(0).getId();
        Stack stack = new Stack();
        stack.setGame(game);
        stack = stackService.saveStack(stack);
        game.getGameStacks().add(stack);
        List<Deckelement> deckelements = deckelementService.getDeckelementsByDeck(deckId);
        for (Deckelement element : deckelements) {
            Card card = new Card();
            card.setName(element.getName());
            card.setSort(element.getSort());
            card.setStack(stack);
            card = cardService.saveCard(card);
            stack.getCards().add(card);
            stackService.saveStack(stack);
        }
    }

    private void convertAndPublishGame(Game game, String sendOnlyToSpecificUser) {
        EntityToDtoConverter converter = new EntityToDtoConverter();

        GameStateMessage gameMessage = new GameStateMessage();
        gameMessage.setCommand(Action.GAME_STATE);

        // Convert Game
        GameDTO gamedto = new GameDTO(game.getId(), game.getTitle(), game.getState());
        gameMessage.setGame(gamedto);

        //Convert Game.Stacks
        for (Stack stack : game.getGameStacks()) {
            StackDTO stackdto = new StackDTO(stack.getId());
            stackdto.setCards(converter.convertCards(stack.getCards(), true));
            gamedto.getStacks().add(stackdto);
        }

        //Convert Game.PlayedCards
        if (game.getPlayedcards() != null){
            gamedto.setPlayedCards(new PlayedCardsDTO());
            gamedto.getPlayedCards().setCards(converter.convertPlayedCards(game.getPlayedcards().getPlayedcards()));
        }

        //Convert Game.Players
        for (Player p1 : game.getPlayers()) {
            for (Player p2 : game.getPlayers()) {

                PlayerDTO playerdto = new PlayerDTO(p2.getId(), p2.getName(), p2.getIsOrganizer(), p2.getPosition(), p2.getPlayerReady());

                //Convert Game.Player.Hand
                if (p2.getHand() != null && p2.getHand().getCards() != null) {
                    HandDTO handdto = new HandDTO();
                    handdto.setId(p2.getHand().getId());
                    if (p1.getId().equals(p2.getId())) {
                        //own hand - generate handcards
                        handdto.setCards(converter.convertCards(p2.getHand().getCards(), false));
                        handdto.setCardCount(p2.getHand().getCards().size());
                    } else {
                        //foreign hand - only count handcards
                        handdto.setCardCount(p2.getHand().getCards().size());
                    }
                    playerdto.setHand(handdto);
                }

                //Convert Game.Player.Stacks
                for (Stack stack : p2.getStacks()) {
                    StackDTO stackdto = new StackDTO(stack.getId());
                    stackdto.setCards(converter.convertCards(stack.getCards(), true));
                    playerdto.getStacks().add(stackdto);
                }
                gamedto.getPlayers().add(playerdto);

                //Convert Game.Action
                List<ch.cas.html5.multicardgame.entity.Action> actions = actionService.getActionsSorted(game.getId());
                if (actions.size() > 0){
                    ActionDTO actiondto = new ActionDTO();
                    actiondto.setId(actions.get(0).getId());
                    actiondto.setPlayerId(actions.get(0).getPlayer().getId());
                    gamedto.setLastAction(actiondto);
                }
            }

            //send to Player p1
            if (sendOnlyToSpecificUser == null){
                System.out.println("Convert and Publish Game for Player: " + p1.getName());
                publishGameToPlayer(gamedto.getId(), p1.getId(), gameMessage);
            }else if(sendOnlyToSpecificUser != null && sendOnlyToSpecificUser.equals(p1.getId())){
                System.out.println("Convert and Publish Game for Player: " + p1.getName());
                publishGameToPlayer(gamedto.getId(), p1.getId(), gameMessage);
            }

            //delete users and build them new
            gamedto.setPlayers(new ArrayList<>());
        }
    }

    private void setPlayerReady(Game game, String playerId) {
        for (Player player : game.getPlayers()) {
            if (player.getId().equals(playerId)) {
                player.setPlayerReady(Boolean.TRUE);
                playerService.savePlayer(player);
            }
        }
    }

    private void publishGameToPlayer(String gameId, String playerId, GameMessage gameMessage) {
        webController.sendToUser(gameId, playerId, gameMessage);
    }

    public void handleMessage(GameMessage gameMessage, String gameId, String playerId) {
        System.out.println("handle incoming message: " + gameMessage.getCommand());
        Game game = gameService.getGame(gameId);

        if (gameMessage.getCommand().equals(Action.CLIENT_GAME_READY)) {
            setGameReady(game);
            convertAndPublishGame(game, null);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_PLAYER_READY)) {
            setPlayerReady(game, playerId);
            convertAndPublishGame(game, null);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_START_GAME)) {
            game.setState(Gamestate.STARTED);
            gameService.updateGame(game);
            if (getFirstGameStack(game).getCards().size() == 36){
                handOutCards(game);
            }
            GameMessage response = new GameMessage();
            response.setCommand(Action.START_GAME);
            publishGameToPlayer(gameId, playerId, response);
            System.out.println("Game started");
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_REQUEST_STATE)) {
            convertAndPublishGame(game, playerId);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_CARD_PLAYED)) {

            PlayedCardMessage msg = (PlayedCardMessage) gameMessage;
            String cardId = msg.getCard().getId();
            Card toPlay = cardService.getCard(cardId);
            System.out.println("Card to play: " + toPlay.getName());
            playCardFromPlayerToPlayedCards(game, playerId, toPlay, Action.CLIENT_CARD_PLAYED);
            convertAndPublishGame(game, null);

        }
        if (gameMessage.getCommand().equals(Action.CLIENT_REVERT_LAST_PLAYER_ACTION)) {
            RevertLastPlayerActionMessage revertAvtioMsg = (RevertLastPlayerActionMessage) gameMessage;
            String cardId = revertAvtioMsg.getCard().getId();
            Card toRevert = cardService.getCard(cardId);
            System.out.println("Revert Action for Card: " + toRevert.getName());
            revertPlayedCardToPlayer(game, playerId, toRevert, Action.CLIENT_CARD_PLAYED);
            convertAndPublishGame(game, null);
        }

        }

    private void playCardFromPlayerToPlayedCards(Game game, String playerId, Card playedCard, Action actionEnum){
        Player player = playerService.getPlayer(playerId);

        //add Card to Player.PlayedCard and remove from Player.Hand
        player.getHand().getCards().remove(playedCard);
        playedCard.setHand(null);
        player.setPlayedCard(playedCard);
        playedCard.setPlayer(player);

        //add card to Game.PlayedCards
        if (game.getPlayedcards() == null){
            game.setPlayedcards(new PlayedCards());
        }
        game.getPlayedcards().setOnSameStack(Boolean.FALSE);
        game.getPlayedcards().getPlayedcards().add(playedCard);
        playedCard.setPlayedcards(game.getPlayedcards());

        //store action
        ch.cas.html5.multicardgame.entity.Action action = new ch.cas.html5.multicardgame.entity.Action();
        action.setGame(game);
        action.setPlayer(player);
        action.setAction(actionEnum);
        action.setCard_id(playedCard.getId());
        action.setSort(actionService.getNextValFromSeq());
        game.getActions().add(action);
        player.getActions().add(action);
    }


    private void revertPlayedCardToPlayer(Game game, String playerId, Card playedCard, Action actionEnum){
        Player player = playerService.getPlayer(playerId);

        //check revert action = last game action
        ch.cas.html5.multicardgame.entity.Action lastAction = actionService.getActionsSorted(game.getId()).get(0);
        if (lastAction != null){
            if (!lastAction.getCard_id().equals(playedCard.getId()) || !lastAction.getPlayer().getId().equals(playerId)){
                System.out.println("Revert Action declined: " + playedCard.getHand());
                return;
            }
        }

        player.getPlayedCard().getPlayedcards().getPlayedcards().remove(playedCard);
        playedCard.setPlayedcards(null);

        player.getHand().getCards().add(playedCard);
        playedCard.setHand(player.getHand());

        //remove card from Game.PlayedCards
        if (game.getPlayedcards() == null){
            game.getPlayedcards().getPlayedcards().remove(playedCard);
        }

        //remove action
        game.getActions().remove(lastAction);
        player.getActions().remove(lastAction);
        lastAction.setGame(null);
        lastAction.setPlayer(null);
        actionService.deleteAction(lastAction.getId());
    }

    private void handOutCards(Game game) {
        System.out.println("hand out cards to players");
        Stack gameStack = getFirstGameStack(game);
        final Set<Card> cards = gameStack.getCards();
        createHandforAllPlayers(game);
        final List<Integer> indexUser = Arrays.asList(1, 2, 3, 4);
        int cnt = 36; //random 0 included bound exclusive
        for (int i = 0; i <= 35; i = i + 1) {
            Card randomCard = generateRandomCard(cards, cnt);

            cards.remove(randomCard);
            removeCardFromStack(randomCard, game);

            //get random card from stack
            cnt = cnt - 1;
            //rotate Player for handout
            Collections.rotate(indexUser, 1);
            Player actualPlayer = getPlayerByPosition(game, indexUser.get(0));
            actualPlayer.getHand().getCards().add(randomCard);
            randomCard.setHand(actualPlayer.getHand());
            randomCard.setStack(null);
            cardService.saveCard(randomCard);
        }
    }

    private Player getPlayerByPosition(Game game, int position){
        for (Player player : game.getPlayers()){
            if (player.getPosition() == position){
                return player;
            }
        }
        return null;
    }

    private Stack getFirstGameStack(Game game){
        Optional<Stack> firstGameStack = game.getGameStacks().stream().findFirst();
        return firstGameStack.get();
    }

    private void removeCardFromStack(Card cardToRemove, Game game) {
        Stack stack = getFirstGameStack(game);
        for (Card stackCard : stack.getCards()) {
            if (stackCard.getId().equals(cardToRemove.getId())) {
                stack.getCards().remove(stackCard);
                stackService.saveStack(stack);
                return;
            }
        }

    }

    private Card generateRandomCard(Set<Card> cards, int bound) {
        Random random = new Random();
        Integer randomInt = random.nextInt(bound);
        int i = 0;
        for (Card card : cards) {
            if (i == randomInt) {
                return card;
            }
            i++;
        }
        return null;
    }

    private void createHandforAllPlayers(Game game) {
        for (Player player : game.getPlayers()) {
            if (player.getHand() == null) {
                Hand hand = new Hand();
                player.setHand(hand);
                hand.setPlayer(player);
                playerService.savePlayer(player);
            }
        }
    }


}
