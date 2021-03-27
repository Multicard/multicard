package ch.cas.html5.multicardgame.dto;

public class PlayerScoreDTO {
    private String playerId;
    private Integer score;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
