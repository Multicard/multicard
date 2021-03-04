package ch.cas.html5.multicardgame.dto;

import java.util.ArrayList;
import java.util.List;

public class HandDTO {
    private String id;
    private int cardCount;
    private List<CardDTO> cards = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }

}
