package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GameRepository extends JpaRepository<Game,String> {

}

