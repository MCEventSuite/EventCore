package dev.imabad.mceventsuite.core.api.objects;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.actions.Action;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.PropertyMapConverter;
import dev.imabad.mceventsuite.core.modules.mysql.dao.RankDAO;
import dev.imabad.mceventsuite.core.util.PropertyMap;
import jdk.nashorn.internal.runtime.Property;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "players")
@DynamicUpdate
public class EventPlayer {

    private static PropertyMap defaultProperties = new PropertyMap();

    public static void addDefault(String name, Object defaultValue){
        defaultProperties.put(name, defaultValue);
    }

    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID uuid;
    private String lastUsername;
    private EventRank rank;
    private List<String> permissions;
    private PropertyMap properties = defaultProperties;

    protected EventPlayer(){}

    protected EventPlayer(UUID uuid, String lastUsername, EventRank rank, List<String> permissions, PropertyMap properties) {
        this.uuid = uuid;
        this.lastUsername = lastUsername;
        this.rank = rank;
        this.permissions = permissions;
        this.properties = properties;
    }

    public EventPlayer(UUID uuid, String username){
        this.uuid = uuid;
        this.lastUsername = username;
        this.rank = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(RankDAO.class).getLowestRank().get();
        this.properties = defaultProperties;
    }

    @Id
    @Column(name="uuid", unique = true, nullable = false)
    @org.hibernate.annotations.Type(type="uuid-char")
    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Column(name = "last_username")
    public String getLastUsername() {
        return lastUsername;
    }

    public void setLastUsername(String lastUsername) {
        this.lastUsername = lastUsername;
    }

    @OneToOne
    @JoinColumn(name="rank_id", referencedColumnName = "id")
    public EventRank getRank() {
        return rank;
    }

    public void setRank(EventRank rank) {
        this.rank = rank;
    }

    @ElementCollection
    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(String permission){
        if(permissions == null){
            permissions = new ArrayList<>();
        }
        this.permissions.add(permission);
    }

    public void removePermission(String permission){
        if(permissions == null){
            return;
        }
        this.permissions.remove(permission);
    }

    public boolean hasPermission(String permission){
        boolean hasPerm = rank.hasPermission(permission);
        if(!hasPerm && this.getPermissions() != null){
            hasPerm = playerHasPermission(permission);
        }
        if(!hasPerm && rank.isInheritsFromBelow()){
            for(EventRank eventRank : EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(RankDAO.class).getRanks()){
                if(eventRank.getPower() <= rank.getPower() && eventRank.hasPermission(permission)){
                    return true;
                }
            }
        }
        return hasPerm;
    }

    public boolean playerHasPermission(String permission){
        if(containsPermission("*")){
            return true;
        }
        String[] parts = permission.split(".");
        StringBuilder fullPerm = new StringBuilder();
        for(int i = 0; i < parts.length - 1; i++){
            String part = parts[i];
            if(i > 0){
                fullPerm.append(".");
            }
            fullPerm.append(part);
            if(containsPermission(fullPerm + ".*")){
                return true;
            }
        }
        return containsPermission(permission);
    }

    private boolean containsPermission(String permission){
        return this.permissions != null && (this.permissions.contains(permission) || (!this.permissions.contains('-' + permission) && this.permissions.contains('+' + permission)));
    }

    @Column(name="properties")
    @Convert(converter = PropertyMapConverter.class)
    public PropertyMap getProperties() {
        return properties;
    }

    public void setProperties(PropertyMap properties) {
        this.properties = properties;
    }

    public int getIntProperty(String name){
        if(properties != null && properties.containsKey(name)){
            return properties.getIntProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getIntProperty(name);
        }
        return -1;
    }

    public String getStringProperty(String name){
        if(properties != null && properties.containsKey(name)){
            return properties.getStringProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getStringProperty(name);
        }
        return null;
    }

    public double getDoubleProperty(String name){
        if(properties != null && properties.containsKey(name)){
            return properties.getDoubleProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getDoubleProperty(name);
        }
        return -1;
    }

    public float getFloatProperty(String name){
        if(properties != null && properties.containsKey(name)){
            return properties.getFloatProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getFloatProperty(name);
        }
        return -1;
    }

    public boolean getBooleanProperty(String name){
        if(properties != null && properties.containsKey(name)){
            return properties.getBooleanProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getBooleanProperty(name);
        }
        return false;
    }

    public Object getProperty(String name){
        if(properties != null && properties.containsKey(name)){
            return properties.get(name);
        } else if(defaultProperties.containsKey(name)){
            return defaultProperties.get(name);
        }
        return null;
    }

    public void setProperty(String name, Object value){
        PropertyMap prop;
        if(this.properties == null){
            prop = new PropertyMap();
        } else {
            prop = this.properties;
        }
        prop.put(name, value);
        this.setProperties(prop);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventPlayer)) return false;
        EventPlayer that = (EventPlayer) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
