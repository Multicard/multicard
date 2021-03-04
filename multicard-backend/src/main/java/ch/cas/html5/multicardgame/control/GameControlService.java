package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.GameDTO;
import ch.cas.html5.multicardgame.dto.HandDTO;
import ch.cas.html5.multicardgame.dto.PlayerDTO;
import ch.cas.html5.multicardgame.dto.StackDTO;
import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameControlService {
    @Autowired
    private GameServiceImpl gameService;
    public void setGameServiceImpl(GameServiceImpl gameService) {
        this.gameService = gameService;
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

    public void setGameReady(String gameId){
        Game game = gameService.getGame(gameId);

        //ToDo - replace fix deck
        String deckId = deckService.retrieveDecks().get(0).getId();

        Stack stack = new Stack();
        stack.setGame(game);
        stack = stackService.saveStack(stack);
        List<Deckelement> deckelements = deckelementService.getDeckelementsByDeck(deckId);
        for(Deckelement element : deckelements){
            Card card = new Card();
            card.setName(element.getName());
            card.setSort(element.getSort());
            card.setStack(stack);
            card = cardService.saveCard(card);
            stack.getCards().add(card);
            stackService.saveStack(stack);
        }
    }

    private void convertAndPublishGame(Game game){
        EntityToDtoConverter converter = new EntityToDtoConverter();

        // Convert Game
        GameDTO gamedto = new GameDTO(game.getId(), game.getTitle());

        //Convert Game.Stacks
        for (Stack stack : game.getGameStacks()){
            StackDTO stackdto = new StackDTO(stack.getId(), (stack.getTopCard() == null ? "" : stack.getTopCard().getName()));
            stackdto.setCards(converter.convertCards(stack.getCards(), true));
            gamedto.getStacks().add(stackdto);
        }

        //Convert Game.Players
        for (Player p1: game.getPlayers()){
            PlayerDTO playerdto = new PlayerDTO(p1.getId(), p1.getName(), p1.getIsOrganizer(), p1.getPosition());
            for (Player p2: game.getPlayers()){
                //Convert Game.Player.Hand
                HandDTO handdto = new HandDTO();
                if (p1.getId().equals(p2.getId())){
                    //own hand - generate handcards
                    handdto.setCards(converter.convertCards(p2.getHand().getCards(), false));
                }else{
                    //foreign hand - only count handcards
                    handdto.setCardCount(p2.getHand().getCards().size());
                }
                playerdto.setHand(handdto);

                //Convert Game.Player.Stacks
                for (Stack stack : p2.getStacks()){
                    StackDTO stackdto = new StackDTO(stack.getId(), (stack.getTopCard() == null ? "" : stack.getTopCard().getName()));
                    stackdto.setCards(converter.convertCards(stack.getCards(), true));
                    playerdto.getStacks().add(stackdto);
                }
            }
            gamedto.getPlayers().add(playerdto);

            //send to Player p1
        }
    }


}
