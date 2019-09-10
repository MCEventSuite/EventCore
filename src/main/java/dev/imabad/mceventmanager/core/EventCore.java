package dev.imabad.mceventmanager.core;

import dev.imabad.mceventmanager.core.api.IConfigProvider;
import dev.imabad.mceventmanager.core.config.EventConfig;
import dev.imabad.mceventmanager.core.registries.ConfigRegistry;
import dev.imabad.mceventmanager.core.registries.DatabaseRegistry;
import dev.imabad.mceventmanager.core.database.MongoDatabase;
import java.io.File;

public class EventCore {

    private static EventCore instance;

    public static EventCore getInstance(){
        return instance;
    }

    private ConfigRegistry configRegistry;
    private DatabaseRegistry databaseRegistry;

    private EventConfig eventConfig;

    public EventCore(File configFolder){
        instance = this;
        configRegistry = new ConfigRegistry(configFolder);
        databaseRegistry = new DatabaseRegistry();
        databaseRegistry.register(MongoDatabase.class, new MongoDatabase());
        databaseRegistry.connectAll();
    }

    public ConfigRegistry getConfigRegistry() {
        return configRegistry;
    }

    public DatabaseRegistry getDatabaseRegistry() {
        return databaseRegistry;
    }

}
