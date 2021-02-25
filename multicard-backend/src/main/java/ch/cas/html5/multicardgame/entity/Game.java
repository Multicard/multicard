package ch.cas.html5.multicardgame.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="game")
public class Game {
    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    private Playground playground;

    private User user;

    private Card card;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ch.cas.html5.multicardgame.entity.Playground getPlayground() {
        return playground;
    }

    public void setPlayground(ch.cas.html5.multicardgame.entity.Playground playground) {
        this.playground = playground;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
