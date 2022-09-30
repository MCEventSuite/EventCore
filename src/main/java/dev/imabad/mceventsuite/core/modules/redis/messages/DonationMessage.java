package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class DonationMessage extends RedisBaseMessage {
    private String username;
    private int amount;
    private boolean plantTree;

    public DonationMessage(String username, int amount, boolean plantTree) {
        this.username = username;
        this.amount = amount;
        this.plantTree = plantTree;
    }

    public String getUsername() {
        return this.username;
    }

    public int getAmount() {
        return this.amount;
    }

    public boolean isPlantTree() {
        return this.plantTree;
    }
}
