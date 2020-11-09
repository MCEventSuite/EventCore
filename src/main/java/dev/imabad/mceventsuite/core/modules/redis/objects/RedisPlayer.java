package dev.imabad.mceventsuite.core.modules.redis.objects;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

public class RedisPlayer {

    public static RedisPlayer fromEventPlayer(EventPlayer eventPlayer, String bungeeName, String server){
        return new RedisPlayer(eventPlayer.getUUID().toString(), eventPlayer.getLastUsername(), bungeeName, server);
    }

    private final String uuid;
    private final String username;
    private final String bungeeName;
    private final String server;

    public RedisPlayer(String uuid, String username, String bungeeName, String server) {
        this.uuid = uuid;
        this.username = username;
        this.bungeeName = bungeeName;
        this.server = server;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getBungeeName() {
        return bungeeName;
    }

    public String getServer() {
        return server;
    }
}
