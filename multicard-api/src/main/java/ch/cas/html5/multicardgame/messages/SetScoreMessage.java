package ch.cas.html5.multicardgame.messages;

import ch.cas.html5.multicardgame.dto.ScoreDTO;

public class SetScoreMessage extends GameMessage{
    private ScoreDTO score;

    public ScoreDTO getScore() {
        return score;
    }

    public void setScore(ScoreDTO score) {
        this.score = score;
    }
}
