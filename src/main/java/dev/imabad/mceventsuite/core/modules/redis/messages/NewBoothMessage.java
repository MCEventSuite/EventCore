package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.api.objects.EventBooth;
import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class NewBoothMessage extends RedisBaseMessage {

    private EventBooth eventBooth;

    public NewBoothMessage(EventBooth eventBooth) {
        this.eventBooth = eventBooth;
    }

    public EventBooth getEventBooth() {
        return eventBooth;
    }
}
