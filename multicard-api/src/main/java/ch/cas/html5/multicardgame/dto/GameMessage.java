package ch.cas.html5.multicardgame.dto;

import ch.cas.html5.multicardgame.enums.Action;

public class GameMessage {
    private Action command;

    private GameDTO game;

    private CardDTO card;

    public Action getCommand() {
        return command;
    }

    public void setCommand(Action command) {
        this.command = command;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }
}
