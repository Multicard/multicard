package ch.cas.html5.multicardgame.messages;

import ch.cas.html5.multicardgame.dto.CardDTO;

public class RevertLastPlayerActionMessage extends GameMessage{
    private CardDTO card;

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }
}
