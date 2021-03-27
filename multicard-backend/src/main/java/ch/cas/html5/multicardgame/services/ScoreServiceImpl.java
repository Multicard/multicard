package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Score;
import ch.cas.html5.multicardgame.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl {
    private final ScoreRepository scoreRepository;

    public ScoreServiceImpl(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public Score saveScore(Score score){
        return scoreRepository.save(score);
    }

    public List<Score> getScoresByPlayers(List<String> playersList) {
        return scoreRepository.getScoreByPlayers(playersList);
    }

    public List<Score> getScoresByPlayer(String playerId) {
        return scoreRepository.getScoreByPlayer(playerId);
    }

    public Score getScoresByPlayerAndRound(String playerId, int round) {
        return scoreRepository.getScoreByPlayerAndRound(playerId, round);
    }
}

