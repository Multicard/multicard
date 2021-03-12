package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.CardDTO;
import ch.cas.html5.multicardgame.dto.PlayedCardDTO;
import ch.cas.html5.multicardgame.entity.Card;

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

    public List<PlayedCardDTO> convertPlayedCards(Set<Card> cards){
        List<PlayedCardDTO> cardsdto = new ArrayList() ;
        for (Card card : cards){
            PlayedCardDTO playedCard = new PlayedCardDTO();
            playedCard.setId(card.getId());
            playedCard.setName(card.getName());
            playedCard.setSort(card.getSort());
            playedCard.setFaceUp(Boolean.TRUE);
            playedCard.setPlayerId(card.getPlayer().getId());
            cardsdto.add(playedCard);
        }
        Collections.sort(cardsdto);
        return cardsdto;
    }
}
