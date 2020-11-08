package dev.imabad.mceventsuite.core.modules.redis.managers;

import dev.imabad.mceventsuite.core.modules.redis.RedisModule;

public class BaseRedisManager {

    protected final RedisModule redisModule;

    public BaseRedisManager(RedisModule redisModule) {
        this.redisModule = redisModule;
    }
}
