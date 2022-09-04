package dev.imabad.mceventsuite.core.api.objects;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "player_years")
public class EventPlayerYear {

    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID id;
    private EventYear year;
    private EventPlayer player;
    private String rank;

    protected EventPlayerYear() {}

    public EventPlayerYear(EventPlayer player, EventYear year, String rank) {
        this.id = UUID.randomUUID();
        this.year = year;
        this.player = player;
        this.rank = rank;
    }

    @Id
    @Column(name="id", unique = true, nullable = false)
    @org.hibernate.annotations.Type(type="uuid-char")
    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    @ManyToOne
    @JoinColumn(name="year", referencedColumnName = "year")
    public EventYear getYear() {
        return year;
    }

    public void setYear(EventYear eventYear) {
        this.year = eventYear;
    }

    @ManyToOne
    @JoinColumn(name="player", referencedColumnName = "uuid")
    public EventPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
