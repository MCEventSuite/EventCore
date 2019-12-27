package dev.imabad.mceventsuite.core.registries;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.BaseConfig;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.IRegistry;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.exceptions.CircularDependencyException;
import dev.imabad.mceventsuite.core.api.exceptions.NotRegisteredException;

import java.util.HashMap;

public class ModuleRegistry implements IRegistry {

    private HashMap<Class<? extends Module>, Module> modules;

    public ModuleRegistry(){
        modules = new HashMap<>();
    }

    public <T extends Module> void addModule(T module){
        modules.put(module.getClass(), module);
    }

    public <T extends Module> T getModule(Class<T> clazz){
        if(!modules.containsKey(clazz)){
            throw new NotRegisteredException(this, clazz.getName());
        }
        return clazz.cast(modules.get(clazz));
    }

    private void loadModuleAndDependencies(Module module){
        if(module.isEnabled()){
           return;
        }
        if(module.getDependencies() != null && module.getDependencies().size() > 0){
            if(module.getDependencies().contains(module)){
                throw new CircularDependencyException(module, module);
            }
            for(Module dependency : module.getDependencies()){
                if(dependency.getDependencies().contains(module)){
                    throw new CircularDependencyException(module, dependency);
                }
                loadModuleAndDependencies(module);
            }
        }
        if(module instanceof IConfigProvider){
            BaseConfig baseConfig = EventCore.getInstance().getConfigLoader().getOrLoadConfig((IConfigProvider) module);
            ((IConfigProvider)module).loadConfig(baseConfig);
        }
        module.onEnable();
    }

    public void enableModules(){
        for(Module module : modules.values()){
            loadModuleAndDependencies(module);
        }
    }

    @Override
    public String getName() {
        return "modules";
    }

    @Override
    public boolean isLoaded() {
        return true;
    }
}
