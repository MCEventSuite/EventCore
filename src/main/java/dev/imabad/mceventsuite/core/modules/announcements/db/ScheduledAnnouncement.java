package dev.imabad.mceventsuite.core.modules.announcements.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "scheduled_announcements")
public class ScheduledAnnouncement implements Comparable<ScheduledAnnouncement> {

    private String id;
    private String message;
    private long interval;
    private long nextRun;

    public ScheduledAnnouncement() {

    }

    public ScheduledAnnouncement(String message, long interval, long nextRun) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.interval = interval;
        this.nextRun = nextRun;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getInterval() {
        return this.interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getNextRun() {
        return this.nextRun;
    }

    public void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    @Override
    public int compareTo(ScheduledAnnouncement other) {
        return Long.compare(this.nextRun, other.nextRun);
    }

}
