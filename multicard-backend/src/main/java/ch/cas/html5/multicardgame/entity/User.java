package ch.cas.html5.multicardgame.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="isAdmin")
    private Boolean isAdmin;

    @JsonIgnoreProperties("players")
    @ManyToOne(optional = true)
    //@JoinColumn(name = "playground_id")
    private Playground playground;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Playground getPlayground() {
        return playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
