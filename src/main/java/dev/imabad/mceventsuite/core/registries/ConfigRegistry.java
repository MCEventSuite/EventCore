package dev.imabad.mceventsuite.core.registries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.imabad.mceventsuite.core.api.BaseConfig;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.IRegistry;
import dev.imabad.mceventsuite.core.api.exceptions.NotRegisteredException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ConfigRegistry implements IRegistry {

    private HashMap<IConfigProvider, Object> configs;
    private Gson gson;
    private File configFolder;

    public ConfigRegistry(File configFolder){
        if(!configFolder.exists()){
            configFolder.mkdir();
        }
        gson = new GsonBuilder().serializeNulls().create();
        configs = new HashMap<>();
        this.configFolder = configFolder;
    }

    public <T extends BaseConfig> void registerConfig(IConfigProvider<T> configProvider){
        File configFile = new File(configFolder, configProvider.getFileName());
        T baseConfig = null;
        if(!configFile.exists()){
            try {
                baseConfig = configProvider.getConfigType().cast(configProvider.getConfigType().newInstance());
                String string = gson.toJson(baseConfig);
                try(FileWriter fileWriter = new FileWriter(configFile)){
                    fileWriter.append(string);
                }catch(IOException e){
                    e.printStackTrace();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
        }
        } else {
            try {
                baseConfig = gson.fromJson(new FileReader(configFile), configProvider.getConfigType());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        configProvider.loadConfig(baseConfig);
        configs.put(configProvider, baseConfig);
    }

    public <T extends BaseConfig> T getConfig(IConfigProvider<T> iConfigProvider) {
        if(!configs.containsKey(iConfigProvider)){
            throw new NotRegisteredException(this, iConfigProvider.getConfig().getName());
        }
        return iConfigProvider.getConfigType().cast(configs.get(iConfigProvider));
    }

    public <T extends BaseConfig> void saveConfig(IConfigProvider<T> iConfigProvider){
        if(!configs.containsKey(iConfigProvider)){
            throw new NotRegisteredException(this, iConfigProvider.getConfig().getName());
        }
        File configFile = new File(configFolder, iConfigProvider.getFileName());
        try {
            gson.toJson(iConfigProvider.getConfig(), iConfigProvider.getConfigType(), new FileWriter(configFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public boolean isLoaded() {
        return true;
    }
}
