package ch.cas.html5.multicardgame.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "connection")
public class Connection {

    @Id
    @Column(unique = true, name = "session_id", nullable = false)
    private String session_id;


    @Column(name = "player_id")
    private String player_id;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String id) {
        this.session_id = id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }
}
