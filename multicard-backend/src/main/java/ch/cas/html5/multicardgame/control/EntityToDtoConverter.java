package ch.cas.html5.multicardgame.control;

import ch.cas.html5.multicardgame.dto.CardDTO;
import ch.cas.html5.multicardgame.entity.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class EntityToDtoConverter {

    public List<CardDTO> convertCards(Set<Card> cards, Boolean anonymous){

        List<CardDTO> cardsdto = new ArrayList() ;
        for (Card card : cards){
            cardsdto.add(new CardDTO(card.getId(), (anonymous ? "N/A" : card.getName()), card.getSort()));
        }
        return cardsdto;
    }
}
