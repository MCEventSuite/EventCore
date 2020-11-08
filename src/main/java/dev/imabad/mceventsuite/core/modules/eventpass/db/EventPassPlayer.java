package dev.imabad.mceventsuite.core.modules.eventpass.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "eventpass_players")
public class EventPassPlayer {

    private EventPlayer player;
    private int xp;

    protected EventPassPlayer(){}

    public EventPassPlayer(EventPlayer player, int initialXP){
        this.player = player;
        this.xp = initialXP;
    }

    @Id
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
        return (int) Math.floor(xp / 1000);
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

}
