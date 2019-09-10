package dev.imabad.mceventmanager.core.api.objects;


import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.Indexes;
import java.util.List;
import org.bson.types.ObjectId;

@Entity("event_ranks")
@Indexes(@Index(value="name", fields = @Field("name")))
public class EventRank {

    @Id
    private ObjectId id;
    private int power;
    private String name;
    private String prefix;
    private String suffix;
    private List<String> permissions;

    EventRank() {
    }

    public EventRank(int power, String name, String prefix, String suffix, List<String> permissions) {
        this.power = power;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
