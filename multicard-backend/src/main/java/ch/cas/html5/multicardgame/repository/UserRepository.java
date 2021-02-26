package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT u FROM User u WHERE u.playground.id = ?1")
    List<User> getUserByPlayground(String playground_id);

}

