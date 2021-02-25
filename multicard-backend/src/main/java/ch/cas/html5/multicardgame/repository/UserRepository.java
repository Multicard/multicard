package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}

