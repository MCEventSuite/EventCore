package dev.imabad.mceventmanager.core.api.objects;

import dev.imabad.mceventmanager.core.database.player.RankDatabase;
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

    @Id
    private UUID uuid;
    private String lastUsername;
    @Reference(lazy = true)
    private EventRank rank;
    private List<String> permissions;

    EventPlayer(){}

    public EventPlayer(UUID uuid, String username){
        this.uuid = uuid;
        this.lastUsername = username;
        this.rank = RankDatabase.getInstance().getLowestRank();
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
}
