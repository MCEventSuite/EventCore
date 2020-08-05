package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.util.GsonUtils;

public class RedisBaseMessage {

    private String sender;
    private long timestamp;
    private String className;

    protected void setSender(String sender){
        this.sender = sender;
    }

    protected void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    protected void setClassName(String className){
        this.className = className;
    }

    public String getSender() {
        return sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getClassName() {
        return className;
    }

    public String toJSON(){
        return GsonUtils.getGson().toJson(this);
    }

}
