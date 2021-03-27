package ch.cas.html5.multicardgame.dto;

import java.util.ArrayList;
import java.util.List;

public class ScoreDTO {
    private String id;
    private Integer round;
    private List<PlayerScoreDTO> playerScores = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public List<PlayerScoreDTO> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<PlayerScoreDTO> playerScores) {
        this.playerScores = playerScores;
    }
}
