package dev.imabad.mceventsuite.core.modules.scavenger.db;

import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassReward;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "scavenger_locations")
public class ScavengerLocation {

    @Id
    private String id;
    private String name;
    private int x;
    private int y;
    private int z;

    protected ScavengerLocation() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
}