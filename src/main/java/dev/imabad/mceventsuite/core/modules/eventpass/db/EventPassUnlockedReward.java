package dev.imabad.mceventsuite.core.modules.eventpass.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "eventpass_unlocked_rewards")
public class EventPassUnlockedReward {

    private int id;
    private EventPlayer player;
    private EventPassReward unlockedReward;
    @CreationTimestamp
    private Date unlockedAt;

    protected EventPassUnlockedReward() {}

    public EventPassUnlockedReward(EventPassReward eventPassReward, EventPlayer player) {
        this.unlockedReward = eventPassReward;
        this.player = player;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = EventPlayer.class)
    public EventPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    @ManyToOne(targetEntity = EventPassReward.class)
    public EventPassReward getUnlockedReward() {
        return unlockedReward;
    }

    public void setUnlockedReward(EventPassReward reward) {
        this.unlockedReward = reward;
    }

}
