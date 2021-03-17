package ch.cas.html5.multicardgame.messages;

import ch.cas.html5.multicardgame.enums.Action;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "messageName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameStateMessage.class, name = "GameStateMessage"),
        @JsonSubTypes.Type(value = PlayedCardMessage.class, name = "PlayedCardMessage"),
        @JsonSubTypes.Type(value = PlayersPositionedMessage.class, name = "PlayersPositionedMessage"),
        @JsonSubTypes.Type(value = RevertLastPlayerActionMessage.class, name = "RevertLastPlayerActionMessage")
})
public class GameMessage {
    private Action command;

    public Action getCommand() {
        return command;
    }

    public void setCommand(Action command) {
        this.command = command;
    }
}
