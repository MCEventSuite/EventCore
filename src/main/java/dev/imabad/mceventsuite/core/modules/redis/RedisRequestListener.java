package dev.imabad.mceventsuite.core.modules.redis;

import java.util.function.Function;

public class RedisRequestListener<T extends RedisBaseMessage, K extends RedisBaseMessage> {

    private final Function<T, K> function;

    public RedisRequestListener(Function<T, K> eventConsumer){
        this.function = eventConsumer;
    }

    public K execute(T event){
        return this.function.apply(event);
    }

}
