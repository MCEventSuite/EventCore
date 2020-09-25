package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.config.database.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisModule extends Module implements IConfigProvider<RedisConfig> {

    private RedisConfig config;
    private RedisConnection redisConnection;
    private Thread redisThread;
    private HashMap<Class<? extends RedisBaseMessage>, List<RedisMessageListener>> registeredListeners = new HashMap<>();

    @Override
    public Class<RedisConfig> getConfigType() {
        return RedisConfig.class;
    }

    @Override
    public RedisConfig getConfig() {
        return config;
    }

    @Override
    public String getFileName() {
        return "redis.json";
    }

    @Override
    public void loadConfig(RedisConfig config) {
        this.config = config;
    }

    @Override
    public void saveConfig() {

    }

    @Override
    public boolean saveOnQuit() {
        return false;
    }

    @Override
    public String getName() {
        return "redis";
    }

    @Override
    public void onEnable() {
        this.redisConnection = new RedisConnection(config);
        redisThread = new Thread(redisConnection::connect);
        redisThread.start();
        this.setEnabled(true);
    }

    @Override
    public void onDisable() {
        if(this.redisConnection.isConnected())
            this.redisConnection.disconnect();
        if(redisThread != null && redisThread.isAlive())
            redisThread.stop();
        this.setEnabled(false);
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Collections.emptyList();
    }

    public <T extends RedisBaseMessage> void registerListener(Class<T> message, RedisMessageListener<T> redisMessageListener){
        if(this.registeredListeners.containsKey(message)){
            this.registeredListeners.get(message).add(redisMessageListener);
        } else {
            this.registeredListeners.put(message, Arrays.asList(redisMessageListener));
        }
    }

    protected <T extends RedisBaseMessage> void handleMessage(T message){
        if(this.registeredListeners.containsKey(message.getClass())){
            this.registeredListeners.get(message.getClass()).forEach(redisMessageListener -> redisMessageListener.execute(message));
        }
    }

    public void publishMessage(RedisChannel channel, RedisBaseMessage message){
        if(!this.redisConnection.isConnected()){
            System.out.println("[EventCore|Redis] Tried to send Redis message but we are not connected");
            return;
        }
        message.setSender(EventCore.getInstance().getIdentifier());
        message.setTimestamp(System.currentTimeMillis());
        message.setClassName(message.getClass().getCanonicalName());
        String messageJSON = message.toJSON();
        if(config.isVerboseLogging()){
            System.out.println("[EventCore|Redis] Sending message to channel " + channel.name() + ": " + messageJSON);
        }
        try(Jedis jedis = redisConnection.getConnection()) {
            jedis.publish(channel.name(), messageJSON);
        }
    }

    public void storeData(String key, String value){
        try(Jedis jedis = redisConnection.getConnection()){
            jedis.set(key, value);
        }
    }

    public void storeData(String key, String value, int expires, TimeUnit timeUnit){
        try(Jedis jedis = redisConnection.getConnection()){
            jedis.set(key, value, SetParams.setParams().ex((int)timeUnit.toSeconds(expires)));
        }
    }

    public String getData(String key){
        try(Jedis jedis = redisConnection.getConnection()){
            return jedis.get(key);
        }
    }

    public void removeData(String key){
        try(Jedis jedis = redisConnection.getConnection()){
            jedis.del(key);
        }
    }
}
