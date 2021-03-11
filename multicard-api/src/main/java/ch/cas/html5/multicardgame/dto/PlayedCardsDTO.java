package ch.cas.html5.multicardgame.dto;

import java.util.ArrayList;
import java.util.List;

public class PlayedCardsDTO {
    private Boolean isOnSameStack;
    private List<PlayedCardDTO> cards = new ArrayList<>();

    public Boolean getOnSameStack() {
        return isOnSameStack;
    }

    public void setOnSameStack(Boolean onSameStack) {
        isOnSameStack = onSameStack;
    }

    public List<PlayedCardDTO> getCards() {
        return cards;
    }

    public void setCards(List<PlayedCardDTO> cards) {
        this.cards = cards;
    }
}
