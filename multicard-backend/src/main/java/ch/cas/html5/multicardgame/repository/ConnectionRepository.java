package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection,String> {

    @Query("SELECT u FROM Connection u Where u.player_id = ?1")
    List<Connection> getConnectionByPlayer(String playerId);


}
