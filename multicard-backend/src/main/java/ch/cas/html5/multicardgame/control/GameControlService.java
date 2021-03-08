package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.*;
import ch.cas.html5.multicardgame.entity.Stack;
import ch.cas.html5.multicardgame.entity.*;
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
    public void setWebSocketController(WebSocketController webController) { this.webController = webController; }

    public void setGameReady(String gameId) {

        Game game = gameService.getGame(gameId);
        if (game == null) {
            return;
        }
        System.out.println("Set Game Ready: " + game.getTitle());

        Set<Stack> stackList = game.getGameStacks();
        game.setGameStacks(new HashSet<>());game.setState(Gamestate.READYTOSTART);
        for (Stack stack : stackList) {
            stackService.deleteStack(stack.getId());
        }

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
        convertAndPublishGame(game);
    }

    private void convertAndPublishGame(Game game) {
        EntityToDtoConverter converter = new EntityToDtoConverter();

        // Convert Game
        GameDTO gamedto = new GameDTO(game.getId(), game.getTitle());

        //Convert Game.Stacks
        for (Stack stack : game.getGameStacks()) {
            StackDTO stackdto = new StackDTO(stack.getId(), (stack.getTopCard() == null ? "" : stack.getTopCard().getName()));
            stackdto.setCards(converter.convertCards(stack.getCards(), true));
            gamedto.getStacks().add(stackdto);
        }

        //Convert Game.Players
        for (Player p1 : game.getPlayers()) {
            for (Player p2 : game.getPlayers()) {

                PlayerDTO playerdto = new PlayerDTO(p2.getId(), p2.getName(), p2.getIsOrganizer(), p2.getPosition());

                //Convert Game.Player.Hand
                if (p2.getHand() != null && p2.getHand().getCards() != null){
                    HandDTO handdto = new HandDTO();
                    handdto.setId(p2.getHand().getId());
                    if (p1.getId().equals(p2.getId())) {
                        //own hand - generate handcards
                        handdto.setCards(converter.convertCards(p2.getHand().getCards(), false));
                    } else {
                        //foreign hand - only count handcards
                        handdto.setCardCount(p2.getHand().getCards().size());
                    }
                    playerdto.setHand(handdto);
                }

                //Convert Game.Player.Stacks
                for (Stack stack : p2.getStacks()) {
                    StackDTO stackdto = new StackDTO(stack.getId(), (stack.getTopCard() == null ? "" : stack.getTopCard().getName()));
                    stackdto.setCards(converter.convertCards(stack.getCards(), true));
                    playerdto.getStacks().add(stackdto);
                }
                gamedto.getPlayers().add(playerdto);
            }

            //send to Player p1
            publishGameToPlayers(gamedto.getId(), p1.getId(), gamedto);

            //delete users and build them new
            gamedto.setPlayers(new ArrayList<>());
        }
    }

    private void publishGameToPlayers(String gameId, String playerId, GameDTO gamedto){
        webController.sendToUser(gameId, playerId, gamedto);
    }

    public void handleMessage(GameAction gameAction, String gameId, String playerId){
        System.out.println("handle incoming message: " + gameAction.getCommand());
        Game game = gameService.getGame(gameId);
        if (!playerId.equals(getGameOrganizer(game).getId())){
            return;
        }

        if (gameAction.getCommand().equals("StartGame")){
            handOutCards(game);
            convertAndPublishGame(game);
        }
    }

    Player getGameOrganizer(Game game){
        for (Player player : game.getPlayers()){
            if (player.getIsOrganizer()){
                return player;
            }
        }
        return null;
    }

    private void handOutCards(Game game) {
        Optional<Stack> firstStack = game.getGameStacks().stream().findFirst();
        final Set<Card> cards = firstStack.get().getCards();
        createHandforAllPlayers(game);
        final List<Integer> indexUser = Arrays.asList(0, 1, 2, 3);
        int cnt = 36; //random 0 included bound exclusive
        for (int i = 0; i <= 35; i = i + 1) {
            Card randomCard = generateRandomInt(cards, cnt);
            //get random card from stack
            //System.out.println(i+1 + " - zufällige Karte: " + card.getName());
            cnt = cnt - 1;
            //rotate Player for handout
            Collections.rotate(indexUser, 1);
            Player actualPlayer = game.getPlayers().get(indexUser.get(0));
            Hand hand = new Hand();
            actualPlayer.getHand().getCards().add(randomCard);

            removeCardFromStack(randomCard, firstStack.get());
            cards.remove(randomCard);
        }
    }

    private void removeCardFromStack(Card cardToRemove, Stack stack){
        int i = 0;
        for (Card stackCard : stack.getCards()){
            if (stackCard.getId().equals(cardToRemove.getId())){
                stack.getCards().remove(i);
                return;
            }
            i++;
        }

    }

    private Card generateRandomInt(Set<Card> cards, int bound) {
        Random random = new Random();
        Integer randomInt =  random.nextInt(bound);
        int i = 0;
        for (Card card : cards){
            if (i == randomInt){
                return card;
            }
            i++;
        }
        return null;
    }

    private void createHandforAllPlayers(Game game){
        for (Player player : game.getPlayers()){
            if (player.getHand() == null){
                Hand hand = new Hand();
                player.setHand(hand);
                hand.setPlayer(player);
                playerService.savePlayer(player);
            }
        }
    }


}
