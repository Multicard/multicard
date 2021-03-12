package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="playedcards")
public class PlayedCards {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @Column(name="isonsamestack")
    private Boolean isOnSameStack = false;

    @JsonIgnoreProperties("playedcards")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "playedcards", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> playedcards = new HashSet<>();

    @OneToOne(mappedBy = "playedcards")
    private Game game;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getOnSameStack() {
        return isOnSameStack;
    }

    public void setOnSameStack(Boolean onSameStack) {
        isOnSameStack = onSameStack;
    }

    public Set<Card> getPlayedcards() {
        return playedcards;
    }

    public void setPlayedcards(Set<Card> playedcards) {
        this.playedcards = playedcards;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
