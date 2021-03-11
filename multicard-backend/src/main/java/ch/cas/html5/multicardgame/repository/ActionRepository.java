package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action,String> {
}