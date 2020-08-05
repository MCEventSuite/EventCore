package dev.imabad.mceventsuite.core.modules.redis;

import com.google.gson.JsonParseException;
import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.config.database.RedisConfig;
import dev.imabad.mceventsuite.core.util.GsonUtils;
import redis.clients.jedis.JedisPubSub;

public class RedisPubSub extends JedisPubSub {

    private RedisConfig redisConfig;

    public RedisPubSub(RedisConfig redisConfig){
        this.redisConfig = redisConfig;
    }

    @Override
    public void onMessage(String channel, String message) {
        if(redisConfig.isVerboseLogging()) {
            System.out.println("[EventCore|Redis] Received message on channel " + channel + ": " + message);
        }
        try {
            RedisBaseMessage redisBaseMessage = GsonUtils.getGson().fromJson(message, RedisBaseMessage.class);
            try{
                Class<? extends RedisBaseMessage> messageClass = (Class<? extends RedisBaseMessage>) Class.forName(redisBaseMessage.getClassName());
                EventCore.getInstance().getModuleRegistry().getModule(RedisModule.class).handleMessage(GsonUtils.getGson().fromJson(message, messageClass));
            }catch(ClassNotFoundException exception){
                System.out.println("[EventCore|Redis] Could not receive message, we don't have it, " + channel + ": " + message);
            }
        }catch(JsonParseException exception){
            System.out.println("[EventCore|Redis] Failed to parse message on channel " + channel + ": " + message);
        }
    }
}
