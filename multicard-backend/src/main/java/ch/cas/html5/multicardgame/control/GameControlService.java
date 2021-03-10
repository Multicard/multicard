package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.*;
import ch.cas.html5.multicardgame.entity.Stack;
import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.enums.Action;
import ch.cas.html5.multicardgame.enums.Gamestate;
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
    private StackServiceImpl stackService;

    public void setStackService(StackServiceImpl stackService) {
        this.stackService = stackService;
    }

    @Autowired
    private WebSocketController webController;

    public void setWebSocketController(WebSocketController webController) {
        this.webController = webController;
    }

    private void resetGame(Game game) {
        System.out.println("reset started: " + game.getPlayers().size());
        game.setState(Gamestate.READYTOSTART);

        Set<Stack> stackList = game.getGameStacks();
        //game.setGameStacks(new HashSet<>());
        for (Stack stack : stackList) {
            game.getGameStacks().remove(stack);
            gameService.updateGame(game);
        }

        Set<Stack> playerStacks = new HashSet<>();
        for (Player player : game.getPlayers()) {
//            for (Stack stack : player.getStacks()) {
//                playerStacks.add(stack);
//                player.getStacks().remove(stack);
//            }
//            for (Stack stack : playerStacks) {
//                stackService.deleteStack(stack.getId());
//            }

            for (Stack stack : player.getStacks()) {
                player.getStacks().remove(stack);
                stackService.deleteStack(stack.getId());
            }
            Hand toDelete = player.getHand();
            if (toDelete != null) {
                player.setHand(null);
                handService.deleteHand(toDelete.getId());
            }
            player.setPlayerReady(false);
            playerService.savePlayer(player);
        }
        System.out.println("reset finished: " + game.getPlayers().size());
    }

    public void getGameAndSetReady(String gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            return;
        }
        setGameReady(game);
        convertAndPublishGame(game);
    }

    public void setGameReady(Game game) {

        System.out.println("Set Game Ready: " + game.getTitle());

        resetGame(game);

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

    private void convertAndPublishGame(Game game) {
        EntityToDtoConverter converter = new EntityToDtoConverter();

        GameMessage gameMessage = new GameMessage();
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

        //Convert Game.Players
        for (Player p1 : game.getPlayers()) {
            System.out.println("Convert and Publish Game for Player: " + p1.getName() + " - Total Player: " + game.getPlayers().size());
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
            }

            //send to Player p1
            publishGameToPlayer(gamedto.getId(), p1.getId(), gameMessage);

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
        System.out.println("handle incoming message: " + gameMessage.getCommand() + " - PlayerId: " + playerId);
        Game game = gameService.getGame(gameId);

        System.out.println("Anz Players: " + game.getPlayers().size());

        if (gameMessage.getCommand().equals(Action.CLIENT_GAME_READY)) {
            setGameReady(game);
            convertAndPublishGame(game);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_PLAYER_READY)) {
            setPlayerReady(game, playerId);
            convertAndPublishGame(game);
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
            System.out.println("Game started: " + game.getPlayers().size());
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_REQUEST_STATE)) {
            convertAndPublishGame(game);
        }
    }

    private void handOutCards(Game game) {
        System.out.println("hand out cards to players");
        Stack gameStack = getFirstGameStack(game);
        final Set<Card> cards = gameStack.getCards();
        createHandforAllPlayers(game);
        final List<Integer> indexUser = Arrays.asList(0, 1, 2, 3);
        int cnt = 36; //random 0 included bound exclusive
        for (int i = 0; i <= 35; i = i + 1) {
            Card randomCard = generateRandomCard(cards, cnt);

            cards.remove(randomCard);
            removeCardFromStack(randomCard, game);

            //get random card from stack
            cnt = cnt - 1;
            //rotate Player for handout
            Collections.rotate(indexUser, 1);
            Player actualPlayer = game.getPlayers().get(indexUser.get(0));
            actualPlayer.getHand().getCards().add(randomCard);
            randomCard.setHand(actualPlayer.getHand());
            randomCard.setStack(null);
            cardService.saveCard(randomCard);
        }
        System.out.println("hand out cards to players finished: " + game.getPlayers().size());
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
