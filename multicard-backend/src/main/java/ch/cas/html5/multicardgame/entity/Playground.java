package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="playground")
public class Playground {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @Column(name="name")
    private String name;

    //https://stackoverflow.com/questions/49130173/how-to-fix-spring-boot-one-to-many-bidirectional-infinity-loop
    @JsonIgnoreProperties("playground")
    @OneToMany(mappedBy = "playground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> players = new ArrayList<>();

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

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public void addPlayer(User user) {
        players.add(user);
        user.setPlayground(this);
    }
}
