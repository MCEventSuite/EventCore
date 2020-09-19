package dev.imabad.mceventsuite.core.api.objects;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "booths")
public class EventBooth {

    private String id;
    private String name;
    private String boothType;
    private EventPlayer owner;
    private List<EventPlayer> members;
    private String plotID;
    private String status = "un-assigned";

    protected EventBooth(){}

    public EventBooth(String name, String boothType, EventPlayer owner, List<EventPlayer> members){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.boothType = boothType;
        this.owner = owner;
        this.members = members;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
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

    @OneToOne(fetch = FetchType.EAGER)
    public EventPlayer getOwner() {
        return owner;
    }

    public void setOwner(EventPlayer owner) {
        this.owner = owner;
    }

    @ManyToMany(fetch = FetchType.EAGER)
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
