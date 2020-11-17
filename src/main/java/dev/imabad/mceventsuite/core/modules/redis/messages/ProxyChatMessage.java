package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class ProxyChatMessage extends RedisBaseMessage {

    private String messageToSend;

    public ProxyChatMessage(String messageToSend) {
        this.messageToSend = messageToSend;
    }

    public String getMessageToSend() {
        return messageToSend;
    }
}
