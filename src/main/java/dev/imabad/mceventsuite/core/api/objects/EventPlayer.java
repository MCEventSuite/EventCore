package dev.imabad.mceventsuite.core.api.objects;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.modules.mongo.MongoModule;
import dev.imabad.mceventsuite.core.util.PropertyMap;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity("event_players")
@Indexes({@Index(value="uuid", fields = @Field("uuid")), @Index(value="username", fields= @Field("lastUsername"))})
public class EventPlayer {

    private static PropertyMap<String, Object> defaultProperties = new PropertyMap<>();

    public static void addDefault(String name, Object defaultValue){
        defaultProperties.put(name, defaultValue);
    }

    @Id
    private UUID uuid;
    private String lastUsername;
    @Reference(lazy = true)
    private EventRank rank;
    private List<String> permissions;
    private PropertyMap<String, Object> properties;

    EventPlayer(){}

    protected EventPlayer(UUID uuid, String lastUsername, EventRank rank, List<String> permissions, PropertyMap<String, Object> properties) {
        this.uuid = uuid;
        this.lastUsername = lastUsername;
        this.rank = rank;
        this.permissions = permissions;
        this.properties = properties;
    }

    public EventPlayer(UUID uuid, String username){
        this.uuid = uuid;
        this.lastUsername = username;
        this.rank = EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().getLowestRank();
        this.properties = defaultProperties;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLastUsername() {
        return lastUsername;
    }

    public void setLastUsername(String lastUsername) {
        this.lastUsername = lastUsername;
    }

    public EventRank getRank() {
        return rank;
    }

    public void setRank(EventRank rank) {
        this.rank = rank;
    }

    public List<String> getPermissions() {
        return permissions;
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
        return this.permissions == null ? rank.getPermissions().contains(permission) : (this.permissions.contains(permission) || (!this.permissions.contains('-' + permission) && this.permissions.contains('+' + permission)));
    }

    public int getIntProperty(String name){
        if(properties.containsKey(name)){
            return properties.getIntProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getIntProperty(name);
        }
        return -1;
    }

    public String getStringProperty(String name){
        if(properties.containsKey(name)){
            return properties.getStringProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getStringProperty(name);
        }
        return null;
    }

    public double getDoubleProperty(String name){
        if(properties.containsKey(name)){
            return properties.getDoubleProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getDoubleProperty(name);
        }
        return -1;
    }

    public float getFloatProperty(String name){
        if(properties.containsKey(name)){
            return properties.getFloatProperty(name);
        } else if (defaultProperties.containsKey(name)){
            return defaultProperties.getFloatProperty(name);
        }
        return -1;
    }

    public Object getProperty(String name){
        if(properties.containsKey(name)){
            return properties.get(name);
        } else if(defaultProperties.containsKey(name)){
            return defaultProperties.get(name);
        }
        return null;
    }
}
