package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.config.database.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

import java.util.Arrays;
import java.util.List;

public class RedisSubscriberThread implements Runnable {

    private RedisConfig redisConfig;
    private JedisPooled subscriber;

    public RedisSubscriberThread(JedisPooled subscriber, RedisConfig redisConfig) {
        this.subscriber = subscriber;
        this.redisConfig = redisConfig;
    }

    @Override
    public void run() {
        this.subscriber.subscribe(new RedisPubSub(this.redisConfig), redisConfig.getChannels().toArray(new String[0]));
    }

}
