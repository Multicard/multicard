package ch.cas.html5.multicardgame.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "position")
    private Integer position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hand_id", referencedColumnName = "id")
    private Hand hand;

    @JsonIgnoreProperties("players")
    @ManyToOne(optional = true)
    private Game game;

    @JsonIgnoreProperties("player")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stack> stacks = new ArrayList<>();

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

    public List<Stack> getStacks() {
        return stacks;
    }

    public void setStacks(List<Stack> stacks) {
        this.stacks = stacks;
    }
}
