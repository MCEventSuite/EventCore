package dev.imabad.mceventsuite.core.config.database;

import dev.imabad.mceventsuite.core.api.database.BaseDatabaseConfig;

public class MySQLConfig extends BaseDatabaseConfig {

    @Override
    public String getName() {
        return "mysql";
    }
}