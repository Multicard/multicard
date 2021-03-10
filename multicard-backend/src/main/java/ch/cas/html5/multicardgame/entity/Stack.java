package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="stack")
public class Stack {
    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @JsonIgnoreProperties("stack")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stack", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    @JsonIgnoreProperties("stacks")
    @ManyToOne(optional = true)
    private Player player;

    @JsonIgnoreProperties("stacks")
    @ManyToOne(optional = true)
    private Game game;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "topcard", referencedColumnName = "id")
//    private Card topCard;
    private String topCard;

    public String getTopCard() {
        return topCard;
    }

    public void setTopCard(String topCard) {
        this.topCard = topCard;
    }

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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

//    public Card getTopCard() {
//        return topCard;
//    }
//
//    public void setTopCard(Card topCard) {
//        this.topCard = topCard;
//    }
}
