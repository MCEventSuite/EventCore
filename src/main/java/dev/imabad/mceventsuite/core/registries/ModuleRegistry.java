package dev.imabad.mceventsuite.core.registries;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.BaseConfig;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.IRegistry;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.exceptions.CircularDependencyException;
import dev.imabad.mceventsuite.core.api.exceptions.NotRegisteredException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleRegistry implements IRegistry {

    private final HashMap<Class<? extends Module>, Module> modules;

    public ModuleRegistry(){
        modules = new HashMap<>();
    }

    public <T extends Module> void addModule(T module){
        modules.put(module.getClass(), module);
    }

    public <T extends Module> void addAndEnableModule(T module){
        if(modules.containsKey(module.getClass()))
            return;
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
                EventCore.getInstance().getConfigLoader().saveConfig(((IConfigProvider<?>) module));
            }
        }
        try {
            module.onDisable();
            module.setEnabled(false);
        }catch(Exception e){
            System.err.printf("[EventCore] Unable to disable module %s\n", module.getName());
            e.printStackTrace();
        }
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

    public <T extends Module> boolean isModuleEnabled(Class<T> moduleClass){
        return modules.containsKey(moduleClass) && modules.get(moduleClass).isEnabled();
    }

    @Override
    public String getName() {
        return "modules";
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    public void loadModulesFromFolder(File modulesFolder){
        if(!modulesFolder.exists()){
            System.out.println("Modules folder does not exist, creating.");
            if(!modulesFolder.mkdir()){
                System.out.println("Failed to create modules folder, not continuing");
                return;
            }
        }
        if(!modulesFolder.isDirectory()){
            System.out.println("Modules folder is not a folder!");
            return;
        }
        System.out.println("Loading modules from modules folder.");
        for (File file : modulesFolder.listFiles()) {
            if(!file.getName().endsWith(".jar")){
                continue;
            }
            String mainModuleClass;
            JarFile jarFile = null;
            InputStream inputStream = null;
            try {
                jarFile = new JarFile(file);
                JarEntry jarEntry = jarFile.getJarEntry("locator.txt");
                if(jarEntry == null){
                    System.out.println("Failed to load module - " + file.getName() + " - Could not find locator.");
                    continue;
                }
                inputStream = jarFile.getInputStream(jarEntry);
                mainModuleClass = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).readLine();
            } catch(Exception e){
                System.out.println("Failed to load module - " + file.getName() + " - " + e.getMessage());
                continue;
            } finally {
                if(jarFile != null)
                    try {
                        jarFile.close();
                    } catch(Exception ignored){}
                if(inputStream != null)
                    try {
                        inputStream.close();
                    } catch(Exception ignored){}
            }
            try {
                URLClassLoader child = new URLClassLoader(
                        new URL[]{file.toURI().toURL()},
                        this.getClass().getClassLoader()
                );
                Class<?> classToLoad = Class.forName(mainModuleClass, true, child);
                if(classToLoad.getSuperclass() != Module.class){
                    System.out.println("Failed to load module - " + file.getName() + " - Invalid main class");
                    continue;
                }
                Module instance = (Module) classToLoad.newInstance();
                addAndEnableModule(instance);
            } catch (Exception e){
                System.out.println("Failed to load module - " + file.getName() + " - " + e.getMessage());
            }
        }
    }
}
