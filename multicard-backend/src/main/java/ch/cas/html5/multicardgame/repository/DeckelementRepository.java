package ch.cas.html5.multicardgame.repository;

import ch.cas.html5.multicardgame.entity.Deckelement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckelementRepository extends JpaRepository<Deckelement,String> {

    @Query("SELECT u FROM Deckelement u WHERE u.deck.id = ?1 order by u.sort")
    List<Deckelement> getDeckelementsByDeck(String deckId);

}
