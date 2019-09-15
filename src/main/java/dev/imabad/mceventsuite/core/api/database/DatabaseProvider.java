package dev.imabad.mceventsuite.core.api.database;

import dev.imabad.mceventsuite.core.api.IConfigProvider;

public abstract class DatabaseProvider<T extends BaseDatabaseConfig> implements IDatabase, IConfigProvider<T> {

    private boolean connected = false;

}
