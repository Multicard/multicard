package ch.cas.html5.multicardgame.dto;

import ch.cas.html5.multicardgame.enums.Action;

public class GameAction {
    private Action command;

    private String text;

    public Action getCommand() {
        return command;
    }

    public void setCommand(Action command) {
        this.command = command;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
