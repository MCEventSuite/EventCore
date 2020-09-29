package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.api.objects.EventBooth;
import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class UpdateBoothMessage extends RedisBaseMessage {

    public enum UpdateAction {
        DELETE,
        UPDATE
    }

    private String boothUUID;
    private UpdateAction action;

    public UpdateBoothMessage(EventBooth booth, UpdateAction action){
        this.boothUUID = booth.getId();
        this.action = action;
    }

    public String getBoothUUID() {
        return boothUUID;
    }

    public UpdateAction getAction(){
        return action;
    }
}
