package ch.cas.html5.multicardgame.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @Column(name = "name")
    private String name;

    @Column(name = "isOrganizer")
    private Boolean isOrganizer;

    @Column(name = "isPlayerReady")
    private Boolean isPlayerReady = false;

    @Column(name = "position")
    private Integer position;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hand_id", referencedColumnName = "id")
    private Hand hand;

    @JsonIgnoreProperties("players")
    @ManyToOne(optional = true)
    private Game game;

    @JsonIgnoreProperties("player")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stack> stacks = new HashSet<>();

    @JsonIgnoreProperties("player")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Action> actions = new HashSet<>();

    @JsonIgnoreProperties("player")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayedCard> playedcards = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Boolean getIsOrganizer() {
        return isOrganizer;
    }

    public void setIsOrganizer(Boolean admin) {
        isOrganizer = admin;
    }

    public Integer getPosition() {return position;}

    public void setPosition(Integer position) {this.position = position;}

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Set<Stack> getStacks() {
        return stacks;
    }

    public void setStacks(Set<Stack> stacks) {
        this.stacks = stacks;
    }

    public Boolean getPlayerReady() {return isPlayerReady; }

    public void setPlayerReady(Boolean playerReady) {isPlayerReady = playerReady; }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    public Set<PlayedCard> getPlayedcards() {
        return playedcards;
    }

    public void setPlayedcards(Set<PlayedCard> playedcards) {
        this.playedcards = playedcards;
    }
}
