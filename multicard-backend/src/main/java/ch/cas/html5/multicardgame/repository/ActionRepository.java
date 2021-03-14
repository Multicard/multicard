package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action,String> {

    @Query(value = "VALUES NEXT VALUE FOR h2_action_seq",  nativeQuery = true)
    Long getNextValueSeq();

    @Query("SELECT u FROM Action u Where u.game.id = ?1 order by u.sort desc")
    List<Action> getActionSorted(String gameId);


}
