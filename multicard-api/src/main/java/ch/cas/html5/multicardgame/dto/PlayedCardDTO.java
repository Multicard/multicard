package ch.cas.html5.multicardgame.dto;

public class PlayedCardDTO extends CardDTO {
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    private String playerId;
}
