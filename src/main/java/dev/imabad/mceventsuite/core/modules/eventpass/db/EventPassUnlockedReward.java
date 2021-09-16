package dev.imabad.mceventsuite.core.modules.eventpass.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "eventpass_unlocked_rewards")
public class EventPassUnlockedReward {

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
    @OneToOne
    @JoinColumn(name="player", referencedColumnName = "uuid")
    public EventPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    @OneToOne
    @JoinColumn(name="unlockedReward", referencedColumnName = "id")
    public EventPassReward getUnlockedReward() {
        return unlockedReward;
    }

    public void setUnlockedReward(EventPassReward reward) {
        this.unlockedReward = reward;
    }

}
