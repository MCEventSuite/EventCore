package dev.imabad.mceventsuite.core.modules.eventpass.db;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "eventpass_codes")
public class EventPassCode {

    @Id
    private String name;
    private EventPassReward reward;

    protected EventPassCode() {
    }

    public EventPassCode(String name, EventPassReward eventPassReward) {
        this.name = name;
        this.reward = eventPassReward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne
    @JoinColumn(name="reward", referencedColumnName = "id")
    public EventPassReward getReward() {
        return reward;
    }

    public void setReward(EventPassReward reward) {
        this.reward = reward;
    }

}