package dev.imabad.mceventsuite.core.modules.redis;

import dev.imabad.mceventsuite.core.util.GsonUtils;

import java.util.UUID;

public class RedisBaseMessage {

    private String sender;
    private long timestamp;
    private String className;
    private String requestId;
    private String responseId;

    protected void setSender(String sender){
        this.sender = sender;
    }

    protected void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    protected void setClassName(String className){
        this.className = className;
    }

    public String setAsRequest() {
        this.requestId = UUID.randomUUID().toString();
        return this.requestId;
    }

    public void setResponseId(String id) {
        this.responseId = id;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getResponseId() {
        return this.responseId;
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
