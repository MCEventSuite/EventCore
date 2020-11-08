package dev.imabad.mceventsuite.core.modules.servers;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.modules.redis.RedisModule;
import dev.imabad.mceventsuite.core.modules.redis.events.RedisConnectionEvent;

import java.util.Collections;
import java.util.List;

public class ServersModule extends Module {

    private ServerRedisManager serverRedisManager;

    @Override
    public String getName() {
        return "servers";
    }

    @Override
    public void onEnable() {
        EventCore.getInstance().getEventRegistry().registerListener(RedisConnectionEvent.class, redisConnectionEvent -> {
            serverRedisManager = new ServerRedisManager(EventCore.getInstance().getModuleRegistry().getModule(RedisModule.class));
        });
    }

    @Override
    public void onDisable() {

    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Collections.emptyList();
    }

    public ServerRedisManager getServerRedisManager() {
        return serverRedisManager;
    }
}
