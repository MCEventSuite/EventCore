package dev.imabad.mceventmanager.core.api.modules;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("modules")
public class ModuleConfig {

    @Id
    private String moduleName;

    public ModuleConfig(){}

    public ModuleConfig(Module module){
        this.moduleName = module.getName();
    }

    public String getModuleName() {
        return moduleName;
    }
}
