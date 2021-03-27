package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="score")
public class Score {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @JsonIgnore
    private Integer round;

    @JsonIgnore
    private int score;

    @JsonIgnore
    private String playerId;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
