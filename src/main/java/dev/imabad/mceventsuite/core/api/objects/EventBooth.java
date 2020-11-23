package dev.imabad.mceventsuite.core.api.objects;

import java.util.List;
import java.util.UUID;

public class EventBooth {

    private String id;
    private String name;
    private String boothType;
    private EventPlayer owner;
    private List<EventPlayer> members;
    private String plotID;
    private String status = "un-assigned";

    public EventBooth(String name, String boothType, EventPlayer owner, List<EventPlayer> members){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.boothType = boothType;
        this.owner = owner;
        this.members = members;
    }

    public EventBooth(String id, String name, String boothType, EventPlayer owner, List<EventPlayer> members, String plotID, String status){
        this(name, boothType, owner, members);
        this.id = id;
        this.plotID = plotID;
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoothType() {
        return boothType;
    }

    public void setBoothType(String boothType) {
        this.boothType = boothType;
    }

    public EventPlayer getOwner() {
        return owner;
    }

    public void setOwner(EventPlayer owner) {
        this.owner = owner;
    }

    public List<EventPlayer> getMembers() {
        return members;
    }

    public void setMembers(List<EventPlayer> members) {
        this.members = members;
    }

    public String getPlotID() {
        return plotID;
    }

    public void setPlotID(String plotID) {
        this.plotID = plotID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
