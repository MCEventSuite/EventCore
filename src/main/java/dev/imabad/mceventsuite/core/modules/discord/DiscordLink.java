package dev.imabad.mceventsuite.core.modules.discord;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Table(name = "discord_links")
public class DiscordLink {

    private String id;
    private EventPlayer player;
    private String discordID;

    public DiscordLink(EventPlayer player, String discordID) {
        this.id = UUID.randomUUID().toString();
        this.player = player;
        this.discordID = discordID;
    }

    protected DiscordLink() {

    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    @OneToOne
    @JoinColumn(name="player", referencedColumnName = "uuid")
    public EventPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EventPlayer player) {
        this.player = player;
    }

    public String getDiscordID() {
        return discordID;
    }

    public void setDiscordID(String discordID) {
        this.discordID = discordID;
    }
}
