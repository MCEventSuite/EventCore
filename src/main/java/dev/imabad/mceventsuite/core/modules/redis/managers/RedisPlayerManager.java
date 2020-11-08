package dev.imabad.mceventsuite.core.modules.redis.managers;

import dev.imabad.mceventsuite.core.modules.redis.RedisModule;
import dev.imabad.mceventsuite.core.modules.redis.objects.RedisPlayer;

import java.util.Set;

public class RedisPlayerManager extends BaseRedisManager{

    private static final String PLAYERS_KEY = "players";

    public RedisPlayerManager(RedisModule redisModule){
        super(redisModule);
    }

    public int playerCount(){
        return redisModule.hashCount(PLAYERS_KEY);
    }

    public Set<RedisPlayer> getPlayers(){
        return redisModule.hashList(PLAYERS_KEY, RedisPlayer.class);
    }

    public void addPlayer(RedisPlayer redisPlayer){
        redisModule.addToHash(PLAYERS_KEY, redisPlayer.getUsername(), redisPlayer);
    }

    public void removePlayer(String username){
        redisModule.removeFromHash(PLAYERS_KEY, username);
    }

    public RedisPlayer getPlayer(String username){
        return redisModule.getFromHash(PLAYERS_KEY, username, RedisPlayer.class);
    }

    public boolean isOnline(String username){
        return redisModule.existsInHash(PLAYERS_KEY, username);
    }
}
