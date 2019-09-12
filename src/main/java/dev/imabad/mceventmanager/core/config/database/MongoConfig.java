package dev.imabad.mceventmanager.core.config.database;

import dev.imabad.mceventmanager.core.api.database.BaseDatabaseConfig;

public class MongoConfig extends BaseDatabaseConfig {

    @Override
    public String getName() {
        return "mongodb";
    }
}