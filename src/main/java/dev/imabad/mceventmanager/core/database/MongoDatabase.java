package dev.imabad.mceventmanager.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.DatabaseProvider;
import dev.imabad.mceventmanager.core.config.database.MongoConfig;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class MongoDatabase extends DatabaseProvider<MongoConfig> {

    private MongoConfig mongoConfig;
    private Morphia morphia;
    private MongoClient mongoClient;
    private Datastore datastore;

    @Override
    public String getName() {
        return "mongodb";
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
        EventCore.getInstance().getConfigRegistry().saveConfig(this);
    }

    @Override
    public void connect() {
        super.connect();
        if(mongoConfig == null || mongoConfig.getHostname() == null || mongoConfig.getHostname().length() < 1){
            throw new RuntimeException("Database config is empty");
        }
        morphia = new Morphia();
        morphia.mapPackage("dev.imabad.mceventmanager.core.api.objects");
        if(mongoConfig.getUsername().length() > 0){
            mongoClient = new MongoClient(new ServerAddress(mongoConfig.getHostname(), mongoConfig.getPort()), MongoCredential.createCredential(mongoConfig.getUsername(), mongoConfig.getDatabase(), mongoConfig.getPassword().toCharArray()), new MongoClientOptions.Builder().build());
        } else {
            mongoClient = new MongoClient(new ServerAddress(mongoConfig.getHostname(), mongoConfig.getPort()), new MongoClientOptions.Builder().build());
        }
        datastore = morphia.createDatastore(mongoClient, mongoConfig.getDatabase());
        datastore.ensureIndexes();
    }

    public void mapPackage(String packageToMap){
        morphia.mapPackage(packageToMap);
    }

    @Override
    public void disconnect() {
        super.disconnect();
        mongoClient.close();
    }

    public Datastore getDatastore() {
        return datastore;
    }
}
