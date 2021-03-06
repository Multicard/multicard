package ch.cas.html5.multicardgame.control;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameAction {
    @JsonProperty("command")
    private String command;

    @JsonProperty("text")
    private String text;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
