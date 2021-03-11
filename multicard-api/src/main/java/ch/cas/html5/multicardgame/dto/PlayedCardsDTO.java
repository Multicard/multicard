package ch.cas.html5.multicardgame.dto;

public class PlayedCardsDTO {
    private Boolean isOnSameStack;
    private PlayedCardDTO[] cards;

    public Boolean getOnSameStack() {
        return isOnSameStack;
    }

    public void setOnSameStack(Boolean onSameStack) {
        isOnSameStack = onSameStack;
    }

    public PlayedCardDTO[] getCards() {
        return cards;
    }

    public void setCards(PlayedCardDTO[] cards) {
        this.cards = cards;
    }
}
