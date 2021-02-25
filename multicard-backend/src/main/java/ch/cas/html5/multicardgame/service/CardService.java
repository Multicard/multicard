package ch.cas.html5.multicardgame.service;

import ch.cas.html5.multicardgame.entity.Card;

import java.util.List;


public interface CardService {

    public List<Card> retrieveCards();

    public Card getCard(Long CardId);

    public void saveCard(Card Card);

    public void deleteCard(Long CardId);

    public void updateCard(Card Card);
}
