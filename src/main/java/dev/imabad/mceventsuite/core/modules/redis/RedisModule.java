package dev.imabad.mceventsuite.core.modules.redis;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RedisModule extends Module implements IConfigProvider<RedisConfig> {

    private RedisConfig config;
    private RedisConnection redisConnection;
    private Thread redisThread;
    private HashMap<Class<? extends RedisBaseMessage>, List<RedisMessageListener>> registeredListeners = new HashMap<>();
    private HashMap<Class<? extends RedisBaseMessage>, List<RedisRequestListener>> requestListeners = new HashMap<>();
    private MutedPlayersManager mutedPlayersManager;
    private RedisPlayerManager redisPlayerManager;

    private Cache<String, Consumer<RedisBaseMessage>> requests = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES).build();

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

    public <T extends RedisBaseMessage, K extends RedisBaseMessage> void registerRequestListener(Class<T> message, RedisRequestListener<T, K> listener) {
        if(this.requestListeners.containsKey(message)){
            this.requestListeners.get(message).add(listener);
        } else {
            this.requestListeners.put(message, Collections.singletonList(listener));
        }
    }

    protected <T extends RedisBaseMessage, K extends RedisBaseMessage> void handleMessage(T message){
        if(message.getResponseId() != null) {
            final Consumer<RedisBaseMessage> consumer = this.requests.getIfPresent(message.getResponseId());
            if(consumer != null) {
                consumer.accept(message);
                return;
            }
        }

        if(message.getRequestId() != null) {
            if(!this.requestListeners.containsKey(message.getClass()))
                return;
            for(RedisRequestListener function : this.requestListeners.get(message.getClass())) {
                RedisBaseMessage response = function.execute(message);
                response.setResponseId(message.getRequestId());
                this.publishMessage(RedisChannel.GLOBAL, response);
                return;
            }
        }

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
        redisConnection.getConnection().publish(channel.name(), messageJSON);
    }

    public void publishRequest(RedisChannel channel, RedisBaseMessage request, Consumer<RedisBaseMessage> consumer) {
        this.requests.put(request.setAsRequest(), consumer);
        this.publishMessage(channel, request);
    }

    public void storeData(String key, String value){
        redisConnection.getConnection().set(key, value);
    }

    public void storeData(String key, String value, int expires, TimeUnit timeUnit){
        redisConnection.getConnection().set(key, value, SetParams.setParams().ex((int)timeUnit.toSeconds(expires)));
    }

    public boolean existsData(String key){
        return redisConnection.getConnection().exists(key);
    }

    public String getData(String key){
        return redisConnection.getConnection().get(key);
    }

    public void removeData(String key){
        redisConnection.getConnection().del(key);
    }

    public int hashCount(String key){
        return (int) redisConnection.getConnection().hlen(key);
    }

    public <T> Set<T> hashList(String key, Class<T> clazz){
        Map<String, String> encodedPlayers = redisConnection.getConnection().hgetAll(key);
        return encodedPlayers.values().stream().map(s -> GsonUtils.getGson().fromJson(s, clazz)).collect(Collectors.toSet());
    }

    public void addToHash(String hashKey, String key, String value){
        redisConnection.getConnection().hset(hashKey, key, value);
    }

    public void addToHash(String hashKey, String key, Object value){
        addToHash(hashKey, key, GsonUtils.getGson().toJson(value));
    }

    public void removeFromHash(String hashKey, String key){
        redisConnection.getConnection().hdel(hashKey, key);
    }

    public boolean existsInHash(String hashKey, String key){
        return redisConnection.getConnection().hexists(hashKey, key);
    }

    public <T> T getFromHash(String hashKey, String value, Class<T> clazz){
        return GsonUtils.getGson().fromJson(redisConnection.getConnection().hget(hashKey, value), clazz);
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
