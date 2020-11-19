package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class ProxyChatMessage extends RedisBaseMessage {

    private String server;
    private String messageToSend;

    public ProxyChatMessage(String server, String messageToSend) {
        this.server = server;
        this.messageToSend = messageToSend;
    }

    public String getServer() {
        return server;
    }

    public String getMessageToSend() {
        return messageToSend;
    }
}
