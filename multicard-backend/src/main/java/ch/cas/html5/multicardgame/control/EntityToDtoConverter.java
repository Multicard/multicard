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

    public List<PlayedCardDTO> convertPlayedCards(Set<Card> cards, List<ch.cas.html5.multicardgame.entity.Action> actions){
        List<PlayedCardDTO> cardsdto = new ArrayList() ;
        for (int i = actions.size(); i > 0; i--) {
            Card c = getCardFromPlayedCards(actions.get(i-1).getCard_id(), cards);
            if (c != null){
                PlayedCardDTO playedCard = new PlayedCardDTO();
                playedCard.setId(c.getId());
                playedCard.setName(c.getName());
                playedCard.setSort(c.getSort());
                playedCard.setFaceUp(Boolean.TRUE);
                playedCard.setPlayerId(c.getPlayer().getId());
                cardsdto.add(playedCard);
            }
        }
        return cardsdto;
    }

    private Card getCardFromPlayedCards(String card_id, Set<Card> cards){
        for (Card card : cards){
            if (card.getId().equals(card_id)){
                return card;
            }
        }
        return null;
    }
}
