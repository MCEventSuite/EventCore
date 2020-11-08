package dev.imabad.mceventsuite.core.modules.servers;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.modules.redis.RedisChannel;
import dev.imabad.mceventsuite.core.modules.redis.RedisModule;
import dev.imabad.mceventsuite.core.modules.redis.managers.BaseRedisManager;
import dev.imabad.mceventsuite.core.modules.servers.objects.RefreshServersMessage;
import dev.imabad.mceventsuite.core.modules.servers.objects.Server;

import java.util.Set;

public class ServerRedisManager extends BaseRedisManager {

    private static final String SERVER_KEY = "servers";

    public ServerRedisManager(RedisModule redisModule) {
        super(redisModule);
    }

    public Set<Server> getServers(){
        return redisModule.hashList(SERVER_KEY, Server.class);
    }

    public boolean serverExists(String name){
       return redisModule.existsInHash(SERVER_KEY, name);
    }

    public void addServer(Server server){
        redisModule.addToHash(SERVER_KEY, server.getName(), server);
        redisModule.publishMessage(RedisChannel.GLOBAL, new RefreshServersMessage());
    }

    public void removeServer(String serverName){
        redisModule.removeFromHash(SERVER_KEY, serverName);
        redisModule.publishMessage(RedisChannel.GLOBAL, new RefreshServersMessage());
    }

    public Server getServer(String serverName){
        return redisModule.getFromHash(SERVER_KEY, serverName, Server.class);
    }

}
