package dev.imabad.mceventmanager.core;

import dev.imabad.mceventmanager.core.config.EventConfig;
import dev.imabad.mceventmanager.core.managers.PlayerManager;
import dev.imabad.mceventmanager.core.modules.mongo.MongoModule;
import dev.imabad.mceventmanager.core.registries.ConfigRegistry;
import dev.imabad.mceventmanager.core.registries.DatabaseRegistry;
import dev.imabad.mceventmanager.core.registries.ModuleRegistry;

import java.io.File;

public class EventCore {

    private static EventCore instance;

    public static EventCore getInstance(){
        return instance;
    }

    private ConfigRegistry configRegistry;
    private ModuleRegistry moduleRegistry;
    private DatabaseRegistry databaseRegistry;

    private PlayerManager playerManager;

    private EventConfig eventConfig;

    public EventCore(File configFolder){
        instance = this;
        configRegistry = new ConfigRegistry(configFolder);
        moduleRegistry = new ModuleRegistry();
        databaseRegistry = new DatabaseRegistry();
        moduleRegistry.addModule(new MongoModule());
        moduleRegistry.enableModules();
        databaseRegistry.connectAll();
        playerManager = new PlayerManager();
    }

    public ConfigRegistry getConfigRegistry() {
        return configRegistry;
    }

    public DatabaseRegistry getDatabaseRegistry() {
        return databaseRegistry;
    }

    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    public EventConfig getEventConfig() {
        return eventConfig;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
