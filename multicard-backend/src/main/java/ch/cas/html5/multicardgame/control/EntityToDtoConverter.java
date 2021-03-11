package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.CardDTO;
import ch.cas.html5.multicardgame.dto.PlayedCardDTO;
import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.entity.PlayedCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class EntityToDtoConverter {

    public List<CardDTO> convertCards(Set<Card> cards, Boolean anonymous){

        List<CardDTO> cardsdto = new ArrayList() ;
        for (Card card : cards){
            cardsdto.add(new CardDTO(card.getId(), (anonymous ? "N/A" : card.getName()), card.getSort(), !anonymous));
        }
        Collections.sort(cardsdto);
        return cardsdto;
    }

    public List<PlayedCardDTO> convertPlayedCards(Set<PlayedCard> playedcards){

        List<PlayedCardDTO> cardsdto = new ArrayList() ;
        for (PlayedCard card : playedcards){
            PlayedCardDTO playeddto = new PlayedCardDTO();
            playeddto.setName(card.getName());
            playeddto.setFaceUp(true);
            playeddto.setSort(card.getSort());
            playeddto.setPlayerId(card.getPlayer().getId());
            cardsdto.add(playeddto);
        }
        return cardsdto;
    }

}
