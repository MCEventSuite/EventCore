package dev.imabad.mceventmanager.core.modules.mongo;

import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.modules.Module;
import dev.imabad.mceventmanager.core.api.modules.ModuleConfig;

import java.util.Collections;
import java.util.List;

public class MongoModule extends Module {

    @Override
    public String getName() {
        return "mongodb";
    }

    @Override
    public void onEnable() {
        EventCore.getInstance().getDatabaseRegistry().register(new MongoDatabase());
    }

    @Override
    public void onDisable() {
        if(EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase() instanceof MongoDatabase && EventCore
                .getInstance().getDatabaseRegistry().getPersistentDatabase().isConnected())
            EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase().disconnect();
    }

    @Override
    public List<Module> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void loadConfig(ModuleConfig moduleConfig) {

    }

    @Override
    public ModuleConfig getConfig() {
        return null;
    }

    @Override
    public boolean hasModuleConfig() {
        return false;
    }
}
