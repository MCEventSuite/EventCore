package dev.imabad.mceventmanager.core.api.database;

import dev.imabad.mceventmanager.core.api.BaseConfig;
import dev.imabad.mceventmanager.core.api.IConfigProvider;

public abstract class DatabaseProvider<T extends BaseDatabaseConfig> implements IDatabase, IConfigProvider<T> {

    private boolean connected = false;

}
