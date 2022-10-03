package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class DonationMessage extends RedisBaseMessage {
    private String username;
    private Type type;
    private boolean plantTree;

    public DonationMessage(String username, Type type, boolean plantTree) {
        this.username = username;
        this.type = type;
        this.plantTree = plantTree;
    }

    public String getUsername() {
        return this.username;
    }

    public Type getType() {
        return this.type;
    }

    public boolean isPlantTree() {
        return this.plantTree;
    }

    public static enum Type {
        VIP,VIPP,DONATION;
    }
}
