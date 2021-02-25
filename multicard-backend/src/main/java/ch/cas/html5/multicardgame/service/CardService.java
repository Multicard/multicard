package ch.cas.html5.multicardgame.service;

import ch.cas.html5.multicardgame.entity.Card;

import java.util.List;


public interface CardService {

    public List<Card> retrieveCards();

    public Card getCard(String cardId);

    public Card saveCard(Card card);

    public void deleteCard(String cardId);

    public void updateCard(Card card);
}
