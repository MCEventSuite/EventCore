package dev.imabad.mceventsuite.core.modules.redis.managers;

import dev.imabad.mceventsuite.core.modules.redis.RedisModule;

public class MutedPlayersManager {

    private static final String MUTED_KEY = "mutedPlayers";

    private final RedisModule redisModule;

    public MutedPlayersManager(RedisModule redisModule){
        this.redisModule = redisModule;
    }

    public int mutedCount(){
        return redisModule.hashCount(MUTED_KEY);
    }

    public void addPlayer(String uuid, long expiry){
        redisModule.addToHash(MUTED_KEY, uuid, expiry);
    }

    public void removePlayer(String uuid){
        redisModule.removeFromHash(MUTED_KEY, uuid);
    }

    public boolean isMuted(String uuid){
        return redisModule.existsInHash(MUTED_KEY, uuid);
    }

    public long getMuteExpiry(String username){
        return redisModule.getFromHash(MUTED_KEY, username, Long.class);
    }

}
