package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.database.DatabaseProvider;
import dev.imabad.mceventsuite.core.api.database.DatabaseType;
import dev.imabad.mceventsuite.core.config.database.RedisConfig;
import dev.imabad.mceventsuite.core.modules.redis.events.RedisConnectionEvent;
import redis.clients.jedis.*;

public class RedisConnection extends DatabaseProvider {

    private RedisConfig config;
    private JedisPooled connection;
    private JedisPooled subscriber;
    private Thread redisSubscriberThread;

    public RedisConnection(RedisConfig config) {
        this.config = config;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.MEMORY;
    }

    @Override
    public String getName() {
        return "redis";
    }

    @Override
    public void connect() {
        subscriber = new JedisPooled(new ConnectionPoolConfig(), config.getHostname(), config.getPort(), 2000,
                config.getPassword().length() == 0 ? null : config.getPassword(), Integer.parseInt(config.getDatabase()));
        if(config.getPassword().length() > 0){
            connection = new JedisPooled(new ConnectionPoolConfig(), config.getHostname(), config.getPort(), 2000, config.getPassword(), Integer.parseInt(config.getDatabase()));
        } else {
            connection = new JedisPooled(new ConnectionPoolConfig(), config.getHostname(), config.getPort(), 2000, null, Integer.parseInt(config.getDatabase()));
        }
        startSubscriberThread();
        EventCore.getInstance().getEventRegistry().handleEvent(new RedisConnectionEvent());
    }

    @Override
    public void disconnect() {
        connection.close();
        subscriber.close();
    }

    public JedisPooled getConnection() {
        return this.connection;
    }

    public void stopThread(){
        if(this.redisSubscriberThread.isAlive()){
            this.redisSubscriberThread.stop();
        }
    }

    public void startSubscriberThread(){
        this.redisSubscriberThread = new Thread(new RedisSubscriberThread(subscriber, config));
        this.redisSubscriberThread.setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            startSubscriberThread();
        });
        this.redisSubscriberThread.start();
    }
}
