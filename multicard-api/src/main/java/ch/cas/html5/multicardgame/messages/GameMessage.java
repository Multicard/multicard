package ch.cas.html5.multicardgame.messages;

import ch.cas.html5.multicardgame.enums.Action;

public class GameMessage {
    private Action command;

    public Action getCommand() {
        return command;
    }

    public void setCommand(Action command) {
        this.command = command;
    }
}
