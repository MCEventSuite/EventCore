package dev.imabad.mceventsuite.core.modules.eventpass.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.eventpass.EventPassModule;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "eventpass_players")
public class EventPassPlayer implements Serializable {

    private EventPlayer player;
    private int xp;

    protected EventPassPlayer(){}

    public EventPassPlayer(EventPlayer player, int initialXP){
        this.player = player;
        this.xp = initialXP;
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

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int levelFromXP(){
        return levelFromXP(this.xp);
    }

    public int levelFromXP(int xp){
        return EventPassModule.levelFromExperience(xp);
    }

    public boolean addXP(int xp){
        int prevXP = this.xp;
        int prevLevel = levelFromXP(prevXP);
        int newXP = prevXP + xp;
        int nextLevel = levelFromXP(newXP);
        this.setXp(newXP);
        if(nextLevel < 51 && nextLevel > prevLevel){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventPassPlayer that = (EventPassPlayer) o;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
