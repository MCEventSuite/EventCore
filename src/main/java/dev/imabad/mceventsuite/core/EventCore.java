package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.config.EventSettings;
import dev.imabad.mceventsuite.core.managers.EventPlayerManager;
import dev.imabad.mceventsuite.core.modules.mongo.MongoLoadedEvent;
import dev.imabad.mceventsuite.core.modules.mongo.MongoModule;
import dev.imabad.mceventsuite.core.registries.EventRegistry;
import dev.imabad.mceventsuite.core.util.ConfigLoader;
import dev.imabad.mceventsuite.core.registries.ModuleRegistry;

import java.io.File;

public class EventCore {

    private static EventCore instance;

    public static EventCore getInstance(){
        return instance;
    }

    private ConfigLoader configLoader;
    private ModuleRegistry moduleRegistry;
    private EventRegistry eventRegistry;

    private EventPlayerManager eventPlayerManager;

    private EventSettings eventSettings;

    public EventCore(File configFolder){
        instance = this;
        configLoader = new ConfigLoader(configFolder);
        moduleRegistry = new ModuleRegistry();
        eventRegistry = new EventRegistry();
        eventRegistry.registerListener(MongoLoadedEvent.class, (event) -> {
            eventSettings = new EventSettings();
            eventPlayerManager = new EventPlayerManager();
        });
        moduleRegistry.addModule(new MongoModule());
        moduleRegistry.enableModules();
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    public EventSettings getEventSettings() {
        return eventSettings;
    }

    public EventPlayerManager getEventPlayerManager() {
        return eventPlayerManager;
    }

    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

}
