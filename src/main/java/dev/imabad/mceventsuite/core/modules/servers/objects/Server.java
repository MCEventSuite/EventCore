package dev.imabad.mceventsuite.core.modules.servers.objects;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

public class Server {

    private String name;
    private String ipAddress;
    private int port;
    private int powerRequired;
    private int priority;

    private boolean online = false;
    private int playerCount = 0;

    public Server(String name, String ipAddress, int port, int powerRequired, int priority) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.port = port;
        this.powerRequired = powerRequired;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPowerRequired() {
        return powerRequired;
    }

    public void setPowerRequired(int powerRequired) {
        this.powerRequired = powerRequired;
    }

    public boolean canJoin(EventPlayer eventPlayer){
        return eventPlayer.getRank().getPower() >= this.powerRequired;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getPriority() {
        return priority;
    }
}
