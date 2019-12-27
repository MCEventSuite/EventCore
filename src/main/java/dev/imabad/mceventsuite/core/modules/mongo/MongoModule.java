package dev.imabad.mceventsuite.core.modules.mongo;

import dev.imabad.mceventsuite.core.api.BaseConfig;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.config.database.MongoConfig;

import java.util.Collections;
import java.util.List;

public class MongoModule extends Module implements IConfigProvider<MongoConfig> {

    private MongoConfig mongoConfig;
    private MongoDatabase mongoDatabase;

    @Override
    public String getName() {
        return "mongodb";
    }

    @Override
    public void onEnable() {
        this.mongoDatabase = new MongoDatabase(this.mongoConfig);
        this.mongoDatabase.connect();
    }

    @Override
    public void onDisable() {
        if(this.mongoDatabase.isConnected())
            this.mongoDatabase.disconnect();
    }

    @Override
    public List<Module> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Class<MongoConfig> getConfigType() {
        return MongoConfig.class;
    }

    @Override
    public MongoConfig getConfig() {
        return mongoConfig;
    }


    @Override
    public String getFileName() {
        return "mongo.json";
    }

    @Override
    public void loadConfig(MongoConfig config) {
        this.mongoConfig = config;
    }

    @Override
    public void saveConfig() {

    }
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
