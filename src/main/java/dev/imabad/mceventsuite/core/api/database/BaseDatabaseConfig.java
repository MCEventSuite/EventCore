package dev.imabad.mceventsuite.core.api.database;

import dev.imabad.mceventsuite.core.api.BaseConfig;

public abstract class BaseDatabaseConfig extends BaseConfig {

    private String hostname = "", username = "", password = "", database = "";
    private int port = 0;

    public String getHostname() {
        return hostname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }
}
