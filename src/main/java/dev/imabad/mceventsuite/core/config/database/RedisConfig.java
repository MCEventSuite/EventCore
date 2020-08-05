package dev.imabad.mceventsuite.core.config.database;

import dev.imabad.mceventsuite.core.api.database.BaseDatabaseConfig;

import java.util.ArrayList;
import java.util.List;

public class RedisConfig extends BaseDatabaseConfig {

    @Override
    public String getName() {
        return "redis";
    }

    private List<String> channels = new ArrayList<>();
    private boolean verboseLogging = false;

    public List<String> getChannels() {
        return channels;
    }

    public boolean isVerboseLogging() {
        return verboseLogging;
    }
}
