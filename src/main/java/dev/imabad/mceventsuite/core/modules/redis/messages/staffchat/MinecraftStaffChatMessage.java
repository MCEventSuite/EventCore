package dev.imabad.mceventsuite.core.modules.redis.messages.staffchat;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class MinecraftStaffChatMessage extends RedisBaseMessage {

    private String username;
    private String message;

    public MinecraftStaffChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
