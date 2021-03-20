package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="hand")
public class Hand {
    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @JsonIgnore
    @OneToOne(mappedBy = "hand")
    private Player player;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hand", cascade = CascadeType.ALL)
    private Set<Card> cards = new HashSet<>();

    public String getId() {return id; }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
