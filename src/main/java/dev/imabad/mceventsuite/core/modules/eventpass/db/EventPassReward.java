package dev.imabad.mceventsuite.core.modules.eventpass.db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "eventpass_rewards")
public class EventPassReward {

    private String id;
    private String name;
    private String description;
    private String image;
    private int requiredLevel;

    protected EventPassReward(){}

    public EventPassReward(String name, String description, String image, int requiredLevel){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.image = image;
        this.requiredLevel = requiredLevel;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
}
