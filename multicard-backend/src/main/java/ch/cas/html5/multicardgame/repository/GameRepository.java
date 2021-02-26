package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface GameRepository extends JpaRepository<Game,String> {

    @Transactional
    @Modifying
    @Query("Delete from Game g WHERE g.user.id = ?1")
    void deleteGameByUser(String userId);


}

