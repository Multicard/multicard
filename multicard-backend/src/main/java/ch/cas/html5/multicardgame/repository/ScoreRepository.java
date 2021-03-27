package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,String> {

    @Query("SELECT u FROM Score u Where u.playerId IN ?1 ORDER BY u.round DESC")
    List<Score> getScoreByPlayers(List<String> playersId);

    @Query("SELECT u FROM Score u Where u.playerId = ?1 ORDER BY u.round DESC")
    List<Score> getScoreByPlayer(String playerId);

    @Query("SELECT u FROM Score u Where u.playerId = ?1 AND u.round = ?2 ORDER BY u.round DESC")
    Score getScoreByPlayerAndRound(String playerId, int round);
}
