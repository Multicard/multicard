package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Hand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface HandRepository extends JpaRepository<Hand,String> {

    @Transactional
    @Modifying
    @Query("Delete from Hand g WHERE g.player.id = ?1")
    void deleteHandByPLayer(String playerId);


}

