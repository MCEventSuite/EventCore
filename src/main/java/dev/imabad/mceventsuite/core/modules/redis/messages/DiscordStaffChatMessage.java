package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class DiscordStaffChatMessage extends RedisBaseMessage {

    private String senderID;
    private String senderUsername;
    private String message;

    public DiscordStaffChatMessage(String senderID, String senderUsername, String message) {
        this.senderID = senderID;
        this.senderUsername = senderUsername;
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getMessage() {
        return message;
    }
}
