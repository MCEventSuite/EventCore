package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.config.database.RedisConfig;
import dev.imabad.mceventsuite.core.modules.redis.events.RedisConnectionEvent;
import dev.imabad.mceventsuite.core.modules.redis.managers.MutedPlayersManager;
import dev.imabad.mceventsuite.core.modules.redis.managers.RedisPlayerManager;
import dev.imabad.mceventsuite.core.modules.redis.objects.RedisPlayer;
import dev.imabad.mceventsuite.core.util.GsonUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisModule extends Module implements IConfigProvider<RedisConfig> {

    private RedisConfig config;
    private RedisConnection redisConnection;
    private Thread redisThread;
    private HashMap<Class<? extends RedisBaseMessage>, List<RedisMessageListener>> registeredListeners = new HashMap<>();
    private MutedPlayersManager mutedPlayersManager;
    private RedisPlayerManager redisPlayerManager;

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
        EventCore.getInstance().getEventRegistry().registerListener(RedisConnectionEvent.class, (event) -> {
            mutedPlayersManager = new MutedPlayersManager(this);
            redisPlayerManager =  new RedisPlayerManager(this);
        });
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
            this.registeredListeners.put(message, Collections.singletonList(redisMessageListener));
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

    public boolean existsData(String key){
        try(Jedis jedis = redisConnection.getConnection()){
            return jedis.exists(key);
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

    public int hashCount(String key){
        try(Jedis jedis = redisConnection.getConnection()){
            return jedis.hlen(key).intValue();
        }
    }

    public <T> Set<T> hashList(String key, Class<T> clazz){
        try(Jedis jedis = redisConnection.getConnection()){
            Map<String, String> encodedPlayers = jedis.hgetAll(key);
            return encodedPlayers.values().stream().map(s -> GsonUtils.getGson().fromJson(s, clazz)).collect(Collectors.toSet());
        }
    }

    public void addToHash(String hashKey, String key, String value){
        try(Jedis jedis = redisConnection.getConnection()){
            jedis.hset(hashKey, key, value);
        }
    }

    public void addToHash(String hashKey, String key, Object value){
        addToHash(hashKey, key, GsonUtils.getGson().toJson(value));
    }

    public void removeFromHash(String hashKey, String key){
        try(Jedis jedis = redisConnection.getConnection()){
            jedis.hdel(hashKey, key);
        }
    }

    public boolean existsInHash(String hashKey, String key){
        try(Jedis jedis = redisConnection.getConnection()){
            return jedis.hexists(hashKey, key);
        }
    }

    public <T> T getFromHash(String hashKey, String value, Class<T> clazz){
        try(Jedis jedis = redisConnection.getConnection()){
            return GsonUtils.getGson().fromJson(jedis.hget(hashKey, value), clazz);
        }
    }

    public MutedPlayersManager getMutedPlayersManager() {
        return mutedPlayersManager;
    }

    public RedisPlayerManager getRedisPlayerManager() {
        return redisPlayerManager;
    }

    public void refreshSubscriber(){
        redisConnection.startSubscriberThread();
        redisConnection.startSubscriberThread();
    }
}
