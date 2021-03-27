package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.*;
import ch.cas.html5.multicardgame.entity.Stack;
import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.enums.Action;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.messages.*;
import ch.cas.html5.multicardgame.messaging.WebSocketController;
import ch.cas.html5.multicardgame.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameControlService {
    private final GameServiceImpl gameService;
    private final PlayerServiceImpl playerService;
    private final DeckServiceImpl deckService;
    private final DeckelementServiceImpl deckelementService;
    private final CardServiceImpl cardService;
    private final ActionServiceImpl actionService;
    private final StackServiceImpl stackService;
    private final GameResetService gameReset;
    private final ConnectionServiceImpl connectionService;

    @Autowired
    private WebSocketController webController;

    public void setWebController(WebSocketController webController) {
        this.webController = webController;
    }


    public GameControlService(GameServiceImpl gameService, PlayerServiceImpl playerService, DeckServiceImpl deckService, DeckelementServiceImpl deckelementService, CardServiceImpl cardService, ActionServiceImpl actionService, StackServiceImpl stackService, GameResetService gameReset, ConnectionServiceImpl connectionService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.deckService = deckService;
        this.deckelementService = deckelementService;
        this.cardService = cardService;
        this.actionService = actionService;
        this.stackService = stackService;
        this.gameReset = gameReset;
        this.connectionService = connectionService;
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

    private void convertAndPublishGame(Game game, String sendOnlyToSpecificUser, Boolean showPlayerStacks) {
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
        if (game.getPlayedcards() != null) {
            gamedto.setPlayedCards(new PlayedCardsDTO());
            List<ch.cas.html5.multicardgame.entity.Action> actions = actionService.getActionsSorted(game.getId());
            gamedto.getPlayedCards().setCards(converter.convertPlayedCards(game.getPlayedcards().getPlayedcards(), actions));
        }

        //Convert Game.Players
        for (Player p1 : game.getPlayers()) {

            //publish only to active users
            if (isPlayerOnline(p1)) {

                for (Player p2 : game.getPlayers()) {

                    PlayerDTO playerdto = new PlayerDTO(p2.getId(), p2.getName(), p2.getIsOrganizer(), p2.getPosition(), p2.getPlayerReady(), isPlayerOnline(p2));

                    //Convert Game.Player.Hand
                    if (p2.getHand() != null && p2.getHand().getCards() != null) {
                        HandDTO handdto = new HandDTO();
                        handdto.setId(p2.getHand().getId());
                        if (p1.getId().equals(p2.getId())) {
                            //own hand - generate handcards
                            handdto.setCards(converter.convertCards(p2.getHand().getCards(), false));
                        }
                        handdto.setCardCount(p2.getHand().getCards().size());
                        playerdto.setHand(handdto);
                    }

                    //Convert Game.Player.Stacks
                    for (Stack stack : p2.getStacks()) {
                        StackDTO stackdto = new StackDTO(stack.getId());
                        Boolean showStackCardsAnonymous = !showPlayerStacks;
                        if (game.getState().equals(Gamestate.ENDED)){
                            showStackCardsAnonymous = false;
                        }
                        stackdto.setCards(converter.convertCards(stack.getCards(), showStackCardsAnonymous));
                        playerdto.getStacks().add(stackdto);
                    }
                    gamedto.getPlayers().add(playerdto);

                    //Convert Game.Action
                    List<ch.cas.html5.multicardgame.entity.Action> actions = actionService.getActionsSorted(game.getId());
                    if (actions.size() > 0) {
                        ActionDTO actiondto = new ActionDTO();
                        actiondto.setId(actions.get(0).getId());
                        actiondto.setPlayerId(actions.get(0).getPlayer().getId());
                        actiondto.setAction(actions.get(0).getAction());
                        gamedto.setLastAction(actiondto);
                    }
                }

                //send to Player p1
                if (sendOnlyToSpecificUser == null) {
                    System.out.println("Convert and Publish Game for Player: " + p1.getName());
                    publishGameToPlayer(gamedto.getId(), p1.getId(), gameMessage);
                } else if (sendOnlyToSpecificUser.equals(p1.getId())) {
                    System.out.println("Convert and Publish Game for Player: " + p1.getName());
                    publishGameToPlayer(gamedto.getId(), p1.getId(), gameMessage);
                }

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
        if (game == null){
            return;
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_GAME_RESET)) {
            setGameReady(game);
            convertAndPublishGame(game, null, false);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_PLAYER_READY)) {
            setPlayerReady(game, playerId);
            convertAndPublishGame(game, null, false);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_START_GAME)) {
            game.setState(Gamestate.STARTED);
            gameService.updateGame(game);
            if (getFirstGameStack(game).getCards().size() == 36) {
                handOutCards(game);
            }
            GameMessage response = new GameMessage();
            response.setCommand(Action.START_GAME);
            for (Player publishPlayer : game.getPlayers()) {
                if (!publishPlayer.getId().equals(playerId)) {
                    publishGameToPlayer(gameId, publishPlayer.getId(), response);
                }
            }
            System.out.println("Game started");
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_REQUEST_STATE)) {
            convertAndPublishGame(game, playerId, false);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_CARD_PLAYED)) {

            PlayedCardMessage msg = (PlayedCardMessage) gameMessage;
            String cardId = msg.getCard().getId();
            Card toPlay = cardService.getCard(cardId);
            System.out.println("Card to play: " + toPlay.getName());
            playCardFromPlayerToPlayedCards(game, playerId, toPlay);
            convertAndPublishGame(game, null, false);

        }
        if (gameMessage.getCommand().equals(Action.CLIENT_REVERT_LAST_PLAYER_ACTION)) {
            RevertLastPlayerActionMessage revertLastActionMsg = (RevertLastPlayerActionMessage) gameMessage;
            revertLastAction(game, revertLastActionMsg.getActionId());
            convertAndPublishGame(game, null, false);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_PLAYED_CARDS_TAKEN)) {
            System.out.println("Player take played Cars to his stack");
            takePlayedCardsToPlayerStack(game, playerId);
            convertAndPublishGame(game, null, false);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_PLAYERS_POSITIONED)) {
            PlayersPositionedMessage playersPositionMsg = (PlayersPositionedMessage) gameMessage;
            System.out.println("New Positions for Players");
            newPositionForPlayers(game, playersPositionMsg);
            convertAndPublishGame(game, null, false);
        }

        if (gameMessage.getCommand().equals(Action.CLIENT_SHOW_ALL_PLAYER_STACKS)) {
            game.setState(Gamestate.ENDED);
            gameService.updateGame(game);
            convertAndPublishGame(game, null, true);
        }
    }

    private Boolean isPlayerOnline(Player player) {
        return (connectionService.getConnectionByPlayer(player.getId()).size() > 0);
    }

    private void newPositionForPlayers(Game game, PlayersPositionedMessage msg) {
        for (Player player : game.getPlayers()) {
            for (PlayerDTO p2 : msg.getPlayers()) {
                if (p2.getId().equals(player.getId())) {
                    player.setPosition(p2.getPosition());
                }
            }
        }

    }

    private void takePlayedCardsToPlayerStack(Game game, String playerId) {
        Player player = playerService.getPlayer(playerId);

        if (player.getStacks().size() == 0) {
            Stack newStack = new Stack();
            player.getStacks().add(newStack);
            newStack.setPlayer(player);
        }
        Set<Card> cardToMove = new HashSet<>(game.getPlayedcards().getPlayedcards());

        Set<String> cardIdList = new HashSet<>();
        for (Card card : cardToMove) {
            game.getPlayedcards().getPlayedcards().remove(card);
            card.getPlayer().getPlayedCards().remove(card);
            card.getPlayedcards().getPlayedcards().remove(card);
            card.setPlayer(null);
            card.setPlayedcards(null);
            cardIdList.add(card.getId());

            if (player.getStacks().stream().findFirst().isPresent()) {
                player.getStacks().stream().findFirst().get().getCards().add(card);
                card.setStack(player.getStacks().stream().findFirst().get());
            }
        }

        //store action
        ch.cas.html5.multicardgame.entity.Action action = new ch.cas.html5.multicardgame.entity.Action();
        action.setGame(game);
        action.setPlayer(player);
        action.setCards_id(cardIdList);
        action.setAction(Action.CLIENT_PLAYED_CARDS_TAKEN);
        action.setSort(actionService.getNextValFromSeq());
        game.getActions().add(action);
        player.getActions().add(action);

    }

    private void playCardFromPlayerToPlayedCards(Game game, String playerId, Card playedCard) {
        Player player = playerService.getPlayer(playerId);

        //add Card to Player.PlayedCard and remove from Player.Hand
        player.getHand().getCards().remove(playedCard);
        playedCard.setHand(null);
        player.getPlayedCards().add(playedCard);
        playedCard.setPlayer(player);

        //add card to Game.PlayedCards
        if (game.getPlayedcards() == null) {
            game.setPlayedcards(new PlayedCards());
        }
        game.getPlayedcards().setOnSameStack(Boolean.FALSE);
        game.getPlayedcards().getPlayedcards().add(playedCard);
        playedCard.setPlayedcards(game.getPlayedcards());

        //store action
        ch.cas.html5.multicardgame.entity.Action action = new ch.cas.html5.multicardgame.entity.Action();
        action.setGame(game);
        action.setPlayer(player);
        action.setAction(Action.CLIENT_CARD_PLAYED);
        action.getCards_id().add(playedCard.getId());
        action.setSort(actionService.getNextValFromSeq());
        game.getActions().add(action);
        player.getActions().add(action);
    }


    private void revertLastAction(Game game, String actionId) {
        List<ch.cas.html5.multicardgame.entity.Action> actionList = actionService.getActionsSorted(game.getId());
        if (actionList.size() < 1) {
            return;
        }
        ch.cas.html5.multicardgame.entity.Action lastAction = actionService.getActionsSorted(game.getId()).get(0);
        //check revert action = last game action
        if (!lastAction.getId().equals(actionId)) {
            System.out.println("Revert Action declined!");
            return;
        }

        if (lastAction.getCards_id().size() == 1) {
            //revert one Card from PlayedCards to Player
            Player player = playerService.getPlayer(lastAction.getPlayer().getId());
            Card playedCard = cardService.getCard(lastAction.getCards_id().stream().findFirst().get());

            player.getPlayedCards().remove(playedCard);
            playedCard.setPlayedcards(null);

            player.getHand().getCards().add(playedCard);
            playedCard.setHand(player.getHand());

            player.getActions().remove(lastAction);

            //remove card from Game.PlayedCards
            if (game.getPlayedcards() != null) {
                game.getPlayedcards().getPlayedcards().remove(playedCard);
            }
        } else {
            //revert 4 Cards from PlayerStack to PlayedCards
            for (String cardId : lastAction.getCards_id()) {

                Card card = cardService.getCard(cardId);
                Player player = getLastPlayerFromActions(card, actionList);
                Stack playerStack = lastAction.getPlayer().getStacks().stream().findFirst().get();

                playerStack.getCards().remove(card);
                card.getStack().getCards().remove(card);
                card.setStack(null);

                game.getPlayedcards().getPlayedcards().add(card);
                card.setPlayer(player);
                card.getPlayer().getPlayedCards().add(card);
                card.setPlayedcards(game.getPlayedcards());
                card.getPlayedcards().getPlayedcards().add(card);
            }
        }

        //remove action
        game.getActions().remove(lastAction);
        if (lastAction != null) {
            lastAction.setGame(null);
            lastAction.setPlayer(null);
            actionService.deleteAction(lastAction.getId());
        }
    }

    private Player getLastPlayerFromActions(Card card, List<ch.cas.html5.multicardgame.entity.Action> actionList){
        for (int i=0; i < actionList.size(); i = i + 1){
            if (actionList.get(i).getAction().equals(Action.CLIENT_CARD_PLAYED)){
                for (String id : actionList.get(i).getCards_id()){
                    if (id.equals(card.getId())){
                        return actionList.get(i).getPlayer();
                    }
                }
            }
        }
        return null;
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
            if (actualPlayer != null) {
                actualPlayer.getHand().getCards().add(randomCard);
            }
            if (randomCard != null) {
                if (actualPlayer != null && actualPlayer.getHand() != null) {
                    randomCard.setHand(actualPlayer.getHand());
                }
            }
            if (randomCard != null) {
                randomCard.setStack(null);
            }
            cardService.saveCard(randomCard);
        }
    }

    private Player getPlayerByPosition(Game game, int position) {
        for (Player player : game.getPlayers()) {
            if (player.getPosition() == position) {
                return player;
            }
        }
        return null;
    }

    private Stack getFirstGameStack(Game game) {
        Optional<Stack> firstGameStack = game.getGameStacks().stream().findFirst();
        return firstGameStack.orElse(null);
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
        int randomInt = random.nextInt(bound);
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

    private String extractUserId(String queue){
        return queue.substring(queue.lastIndexOf("/") + 1, queue.length());
    }

    public void addConnection(String queue, String sessionId){
        if (connectionService.getConnection(sessionId) != null){
            return;
        }
        String playerId = extractUserId(queue);
        Game game = getGameForConnection(playerId);
        if (game == null){
            return;
        }
        Connection connection = new Connection();
        connection.setSession_id(sessionId);
        connection.setPlayer_id(playerId);
        connectionService.saveConnection(connection);
        convertAndPublishGame(game, null, false);
    }

    public void removeConnection(String queue, String sessionId){
        Connection connection = connectionService.getConnection(sessionId);
        if (connection == null){
            return;
        }
        connectionService.deleteConnection(connection);
        Game game = getGameForConnection(connection.getPlayer_id());
        if (game == null){
            return;
        }
        convertAndPublishGame(game, null, false);
    }

    public void playerOff(String sessionId){
        removeConnection(null, sessionId);
    }

    private Game getGameForConnection(String playerId){
        Player player = playerService.getPlayer(playerId);
        if (player != null && player.getGame() != null){
            return player.getGame();
        }
        return null;
    }

}
