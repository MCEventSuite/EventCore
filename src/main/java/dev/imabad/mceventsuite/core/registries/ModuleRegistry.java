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

    private final HashMap<Class<? extends Module>, Module> modules;

    public ModuleRegistry(){
        modules = new HashMap<>();
    }

    public <T extends Module> void addModule(T module){
        modules.put(module.getClass(), module);
    }

    public <T extends Module> void addAndEnableModule(T module){
        modules.put(module.getClass(), module);
        loadModuleAndDependencies(module);
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
            for(Class<? extends Module> dependency : module.getDependencies()){
                if(modules.containsKey(dependency)){
                    Module dependencyModule = modules.get(dependency);
                    if(dependencyModule.getDependencies().contains(module)){
                        throw new CircularDependencyException(module, dependencyModule);
                    }
                    if(!dependencyModule.isEnabled()){
                        loadModuleAndDependencies(module);
                    }
                } else {
                    try {
                        addAndEnableModule(dependency.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(module instanceof IConfigProvider){
            BaseConfig baseConfig = EventCore.getInstance().getConfigLoader().getOrLoadConfig((IConfigProvider<?>) module);
            ((IConfigProvider)module).loadConfig(baseConfig);
        }

        try {
            module.onEnable();
            module.setEnabled(true);
        } catch (Exception ex) {
            System.err.printf("[EventCore] Unable to enable module %s\n", module.getName());
            ex.printStackTrace();

            // TODO(oliver): Add support for optional modules
            // TODO(oliver): Call some sort of abstract shutdown hook, instead of System.exit
            System.exit(1);
        }
    }

    private void unloadModule(Module module){
        if(!module.isEnabled()){
            return;
        }
        if(module instanceof IConfigProvider){
            if(((IConfigProvider<?>) module).saveOnQuit()){
                ((IConfigProvider<?>) module).saveConfig();
            }
        }

        module.onDisable();
        module.setEnabled(false);
    }

    public void enableModules(){
        for(Module module : modules.values()){
            loadModuleAndDependencies(module);
        }
    }

    public void disableModules(){
        for(Module module : modules.values()){
            unloadModule(module);
        }
    }

    public <T extends Module> void enableModule(T module){
        loadModuleAndDependencies(module);
    }

    public <T extends Module> void disableModule(T module){
        unloadModule(module);
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
