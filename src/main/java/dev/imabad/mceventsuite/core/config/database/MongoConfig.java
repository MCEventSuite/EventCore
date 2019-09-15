package dev.imabad.mceventsuite.core.config.database;

import dev.imabad.mceventsuite.core.api.database.BaseDatabaseConfig;

public class MongoConfig extends BaseDatabaseConfig {

    @Override
    public String getName() {
        return "mongodb";
    }
}