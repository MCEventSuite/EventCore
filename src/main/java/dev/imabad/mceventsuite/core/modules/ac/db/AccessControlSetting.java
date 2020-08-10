package dev.imabad.mceventsuite.core.modules.ac.db;

import dev.imabad.mceventsuite.core.api.objects.EventRank;

import javax.persistence.*;

@Entity
@Table(name = "access_control")
public class AccessControlSetting {

    private int id;
    private EventRank rank;
    private long unlockTime;
    private String denyMessage;

    public AccessControlSetting(){}

    public AccessControlSetting(EventRank rank, long unlockTime, String denyMessage){
        this.rank = rank;
        this.unlockTime = unlockTime;
        this.denyMessage = denyMessage;
    }

    public AccessControlSetting(EventRank rank){
        this(rank, -1, "You do not have access to this");
    }

    public AccessControlSetting(EventRank rank, long unlockTime){
        this(rank, unlockTime, "You do not have access to this");
    }

    public AccessControlSetting(EventRank rank, String denyMessage){
        this(rank, -1, denyMessage);
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId() {
        return id;
    }

    @OneToMany
    @JoinColumn(name="rank_id", referencedColumnName = "id")
    public EventRank getRank() {
        return rank;
    }

    public void setRank(EventRank rank) {
        this.rank = rank;
    }

    public long getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(long unlockTime) {
        this.unlockTime = unlockTime;
    }

    public String getDenyMessage() {
        return denyMessage;
    }

    public void setDenyMessage(String denyMessage) {
        this.denyMessage = denyMessage;
    }
}
