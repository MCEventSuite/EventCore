package dev.imabad.mceventsuite.core.api.modules;

import java.util.Collections;
import java.util.List;

public abstract class Module {

    private boolean enabled = false;

    public abstract String getName();

    public abstract void onEnable();

    public abstract void onDisable();

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public abstract List<Class<? extends Module>> getDependencies();

    public List<String> getPermissions(){
        return Collections.emptyList();
    }
}
