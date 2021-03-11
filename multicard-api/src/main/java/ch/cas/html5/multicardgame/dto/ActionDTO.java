package ch.cas.html5.multicardgame.dto;

import ch.cas.html5.multicardgame.enums.Action;

public class ActionDTO {
    private String id;
    private String playerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    private Action action;
}
