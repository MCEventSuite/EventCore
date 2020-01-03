package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.api.actions.IActionExecutor;
import dev.imabad.mceventsuite.core.config.EventSettings;
import dev.imabad.mceventsuite.core.managers.EventPlayerManager;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.registries.EventRegistry;
import dev.imabad.mceventsuite.core.registries.ModuleRegistry;
import dev.imabad.mceventsuite.core.util.ConfigLoader;

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

    private IActionExecutor actionExecutor;
    private EventSettings eventSettings;

    public EventCore(File configFolder){
        instance = this;
        configLoader = new ConfigLoader(configFolder);
        moduleRegistry = new ModuleRegistry();
        eventRegistry = new EventRegistry();
        moduleRegistry.addModule(new MySQLModule());
        moduleRegistry.enableModules();
        eventSettings = new EventSettings();
        eventPlayerManager = new EventPlayerManager();
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

    public void setActionExecutor(IActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public IActionExecutor getActionExecutor() {
        return actionExecutor;
    }
}
