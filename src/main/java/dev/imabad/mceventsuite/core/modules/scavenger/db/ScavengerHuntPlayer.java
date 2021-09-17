package dev.imabad.mceventsuite.core.modules.scavenger.db;


import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.eventpass.EventPassModule;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "scavengerhunt_players")
public class ScavengerHuntPlayer implements Serializable {

    private EventPlayer player;
    private Set<ScavengerLocation> foundLocations;

    protected ScavengerHuntPlayer(){}

    public ScavengerHuntPlayer(EventPlayer player){
        this.player = player;
    }

    @Id
    @OneToOne
    @JoinColumn(name="player", referencedColumnName = "uuid")
    public EventPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    @ManyToMany(targetEntity = ScavengerLocation.class)
    public Set<ScavengerLocation> getFoundLocations() {
        return foundLocations;
    }

    public void setFoundLocations(Set<ScavengerLocation> foundLocations) {
        this.foundLocations = foundLocations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        dev.imabad.mceventsuite.core.modules.scavenger.db.ScavengerHuntPlayer that = (ScavengerHuntPlayer) o;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}

