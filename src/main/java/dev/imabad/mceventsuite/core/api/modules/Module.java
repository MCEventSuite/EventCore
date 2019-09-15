package dev.imabad.mceventsuite.core.api.modules;

import com.mongodb.lang.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class Module {

    public boolean enabled = false;

    public abstract String getName();

    public abstract void onEnable();

    public abstract void onDisable();

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public abstract List<Module> getDependencies();

    public boolean hasModuleConfig() { return true; }

    public abstract void loadConfig(@Nullable ModuleConfig moduleConfig);

    public abstract ModuleConfig getConfig();

    public List<String> getPermissions(){
        return Collections.emptyList();
    }
}
