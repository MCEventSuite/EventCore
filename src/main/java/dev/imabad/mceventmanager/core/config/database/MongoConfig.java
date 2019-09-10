package dev.imabad.mceventmanager.core.config.database;

import dev.imabad.mceventmanager.core.api.BaseConfig;

public class MongoConfig extends BaseConfig {

    private String hostname = "", username = "", password = "", database = "";
    private int port = 0;

    @Override
    public String getName() {
        return "mongodb";
    }

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