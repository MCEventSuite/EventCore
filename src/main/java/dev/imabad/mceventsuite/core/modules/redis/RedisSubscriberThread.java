package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.config.database.RedisConfig;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

public class RedisSubscriberThread implements Runnable {

    private RedisConfig redisConfig;
    private Jedis subscriber;

    public RedisSubscriberThread(Jedis subscriber, RedisConfig redisConfig) {
        this.subscriber = subscriber;
        this.redisConfig = redisConfig;
    }

    @Override
    public void run() {
        try {
            this.subscriber.subscribe(new RedisPubSub(this.redisConfig), redisConfig.getChannels().toArray(new String[0]));
        } catch(Exception ignored){}
    }

}
