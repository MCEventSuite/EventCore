package dev.imabad.mceventsuite.core.modules.redis;

import java.util.function.Consumer;

public class RedisMessageListener<T extends RedisBaseMessage> {

    private final Consumer<T> consumer;

    public RedisMessageListener(Consumer<T> eventConsumer){
        this.consumer = eventConsumer;
    }

    public void execute(T event){
        this.consumer.accept(event);
    }

}
