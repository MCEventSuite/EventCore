package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.actions.IActionExecutor;
import dev.imabad.mceventsuite.core.config.CoreConfig;
import dev.imabad.mceventsuite.core.config.EventSettings;
import dev.imabad.mceventsuite.core.events.CoreShutdownEvent;
import dev.imabad.mceventsuite.core.managers.EventPlayerManager;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import dev.imabad.mceventsuite.core.registries.EventRegistry;
import dev.imabad.mceventsuite.core.registries.ModuleRegistry;
import dev.imabad.mceventsuite.core.util.ConfigLoader;

import java.io.File;

public class EventCore implements IConfigProvider<CoreConfig> {

    private static EventCore instance;

    public static EventCore getInstance(){
        return instance;
    }

    private ConfigLoader configLoader;
    private ModuleRegistry moduleRegistry;
    private EventRegistry eventRegistry;

    private CoreConfig config;

    private EventPlayerManager eventPlayerManager;

    private IActionExecutor actionExecutor;
    private EventSettings eventSettings;

    private String identifier;

    public EventCore(File configFolder){
        instance = this;
        configLoader = new ConfigLoader(configFolder);
        configLoader.getOrLoadConfig(this);
        eventRegistry = new EventRegistry();
        eventRegistry.registerListener(MySQLLoadedEvent.class, (event) -> {
            eventSettings = new EventSettings();
            eventPlayerManager = new EventPlayerManager();
        });
        moduleRegistry = new ModuleRegistry();
        moduleRegistry.addAndEnableModule(new MySQLModule());
        moduleRegistry.loadModulesFromFolder(new File(configFolder, "modules"));
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

    public void shutdown(){
        System.out.println("[EventCore] Shutting down core");
        eventRegistry.handleEvent(new CoreShutdownEvent());
        moduleRegistry.disableModules();
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Class<CoreConfig> getConfigType() {
        return CoreConfig.class;
    }

    @Override
    public CoreConfig getConfig() {
        return config;
    }

    @Override
    public String getFileName() {
        return "core.json";
    }

    @Override
    public void loadConfig(CoreConfig config) {
        this.config = config;
        this.identifier = config.getIdentifier();
    }

    @Override
    public void saveConfig() {

    }

    @Override
    public boolean saveOnQuit() {
        return false;
    }
}
