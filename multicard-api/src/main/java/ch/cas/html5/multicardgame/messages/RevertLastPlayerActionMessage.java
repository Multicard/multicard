package ch.cas.html5.multicardgame.messages;

public class RevertLastPlayerActionMessage extends GameMessage{
    private String actionId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
