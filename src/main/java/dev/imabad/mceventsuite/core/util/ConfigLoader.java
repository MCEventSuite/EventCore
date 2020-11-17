package dev.imabad.mceventsuite.core.util;

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

public class ConfigLoader {

    private HashMap<String, Object> configs;
    private Gson gson;
    private File configFolder;

    public ConfigLoader(File configFolder){
        if(!configFolder.exists()){
            configFolder.mkdir();
        }
        gson = new GsonBuilder().serializeNulls().create();
        configs = new HashMap<>();
        this.configFolder = configFolder;
    }

    private <T extends BaseConfig> T loadConfig(IConfigProvider<T> iConfigProvider){
        File configFile = new File(configFolder, iConfigProvider.getFileName());
        T baseConfig = null;
        if(!configFile.exists()){
            try {
                baseConfig = iConfigProvider.getConfigType().cast(iConfigProvider.getConfigType().newInstance());
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
                baseConfig = gson.fromJson(new FileReader(configFile), iConfigProvider.getConfigType());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        iConfigProvider.loadConfig(baseConfig);
        configs.put(iConfigProvider.getFileName(), baseConfig);
        return baseConfig;
    }

    public <T extends BaseConfig> T getOrLoadConfig(IConfigProvider<T> iConfigProvider) {
        if(!configs.containsKey(iConfigProvider)){
            return this.loadConfig(iConfigProvider);
        }
        return iConfigProvider.getConfigType().cast(configs.get(iConfigProvider));
    }

    public <T extends BaseConfig> void saveConfig(IConfigProvider<T> iConfigProvider){
        File configFile = new File(configFolder, iConfigProvider.getFileName());
        T baseConfig = iConfigProvider.getConfigType().cast(iConfigProvider.getConfig());
        String string = gson.toJson(baseConfig, iConfigProvider.getConfigType());
        try(FileWriter fileWriter = new FileWriter(configFile)){
            fileWriter.append(string);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
