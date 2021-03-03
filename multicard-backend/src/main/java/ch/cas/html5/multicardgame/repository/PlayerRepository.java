package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayerRepository extends JpaRepository<Player,String> {

    @Query("SELECT u FROM Player u WHERE u.game.id = ?1")
    List<Player> getPlayersByGame(String gameId);

}

