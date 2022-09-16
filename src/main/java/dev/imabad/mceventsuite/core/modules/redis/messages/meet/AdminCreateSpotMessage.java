package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AdminCreateSpotMessage extends RedisBaseMessage {
    private String name;
    private int position;
    private double x;
    private double y;
    private double z;

    public AdminCreateSpotMessage(String name, int position, double x, double y, double z){
        this.name = name;
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return this.name;
    }

    public int getPosition() {
        return this.position;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}
