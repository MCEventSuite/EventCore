package dev.imabad.mceventsuite.core.modules.audit.db;


import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.audit.ActionType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
public class AuditLogEntry {

    private String id;
    private EventPlayer actionedPlayer;
    private EventPlayer actionerPlayer;
    private ActionType actionType;
    private String reason;
    private long timeStamp;

    public AuditLogEntry(EventPlayer actionedPlayer, EventPlayer actionerPlayer, ActionType actionType, String reason, long timeStamp) {
        this.id = UUID.randomUUID().toString();
        this.actionedPlayer = actionedPlayer;
        this.actionerPlayer = actionerPlayer;
        this.actionType = actionType;
        this.reason = reason;
        this.timeStamp = timeStamp;
    }

    protected AuditLogEntry() {

    }

    @OneToOne
    @JoinColumn(name="actionedPlayer", referencedColumnName = "uuid")
    public EventPlayer getActionedPlayer() {
        return actionedPlayer;
    }

    public void setActionedPlayer(EventPlayer actionedPlayer) {
        this.actionedPlayer = actionedPlayer;
    }

    @OneToOne
    @JoinColumn(name="actionerPlayer", referencedColumnName = "uuid")
    public EventPlayer getActionerPlayer() {
        return actionerPlayer;
    }

    public void setActionerPlayer(EventPlayer actionerPlayer) {
        this.actionerPlayer = actionerPlayer;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }
}
