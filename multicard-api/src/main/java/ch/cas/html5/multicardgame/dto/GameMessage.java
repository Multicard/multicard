package ch.cas.html5.multicardgame.dto;

import ch.cas.html5.multicardgame.enums.Action;

public class GameMessage {
    private Action command;

    private GameDTO game;

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
}
