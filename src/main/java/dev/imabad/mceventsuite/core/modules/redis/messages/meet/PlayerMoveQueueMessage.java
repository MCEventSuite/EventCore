package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.UUID;

public class PlayerMoveQueueMessage extends RedisBaseMessage {
    private String name;
    private UUID uuid;
    private boolean isQueue;
    private int position;
    private int eta;
    private Location location;

    public PlayerMoveQueueMessage(String name, UUID uuid, boolean isQueue, int position, int eta, Location location) {
        this.name = name;
        this.uuid = uuid;
        this.isQueue = isQueue;
        this.position = position;
        this.eta = eta;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isQueue() {
        return this.isQueue;
    }

    public int getPosition() {
        return this.position;
    }

    public int getEta() {
        return this.eta;
    }

    public Location getLocation() {
        return this.location;
    }

    public static class Location {
        private double x;
        private double y;
        private double z;

        public Location(double x, double y, double z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}
