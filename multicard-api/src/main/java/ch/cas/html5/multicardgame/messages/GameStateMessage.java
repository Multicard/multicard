package ch.cas.html5.multicardgame.messages;

import ch.cas.html5.multicardgame.dto.GameDTO;

public class GameStateMessage extends GameMessage {
    private GameDTO game;

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}
