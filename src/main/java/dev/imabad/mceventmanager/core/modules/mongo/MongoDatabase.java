package dev.imabad.mceventmanager.core.modules.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.database.DatabaseProvider;
import dev.imabad.mceventmanager.core.api.database.DatabaseType;
import dev.imabad.mceventmanager.core.api.database.IPersistentDatabase;
import dev.imabad.mceventmanager.core.api.objects.EventPlayer;
import dev.imabad.mceventmanager.core.api.objects.EventRank;
import dev.imabad.mceventmanager.core.config.database.MongoConfig;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Sort;

import java.util.List;
import java.util.UUID;

public class MongoDatabase extends DatabaseProvider<MongoConfig> implements IPersistentDatabase {

    private boolean connected = false;
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
        this.connected = true;
    }

    public void mapPackage(String packageToMap){
        morphia.mapPackage(packageToMap);
    }

    @Override
    public void disconnect() {
        mongoClient.close();
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.PERSISTENT;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    @Override
    public EventPlayer getOrCreatePlayer(UUID uuid, String username) {
        EventPlayer player = getPlayer(uuid);
        if(player == null){
            player = getPlayer(username);
            if(player == null){
                player = new EventPlayer(uuid, username);
                savePlayer(player);
            }
        }
        return player;
    }

    @Override
    public EventPlayer getPlayer(String username) {
       return datastore.createQuery(EventPlayer.class).field("lastUsername").equalIgnoreCase(username).first();
    }

    @Override
    public EventPlayer getPlayer(UUID uuid) {
        return datastore.createQuery(EventPlayer.class).field("uuid").equal(uuid).first();
    }

    @Override
    public void savePlayer(EventPlayer player) {
        save(player);
    }

    @Override
    public List<EventPlayer> getPlayers() {
        return datastore.createQuery(EventPlayer.class).asList();
    }

    @Override
    public EventRank getLowestRank() {
        return datastore.createQuery(EventRank.class).order(Sort.descending("power")).first();
    }

    @Override
    public List<EventRank> getRanks() {
        return datastore.createQuery(EventRank.class).order(Sort.descending("power")).asList();
    }

    @Override
    public void saveRank(EventRank eventRank) {
        save(eventRank);
    }

    @Override
    public Object get(Object object) {
        return datastore.createQuery(object.getClass()).first();
    }

    @Override
    public void save(Object object) {
        datastore.save(object);
    }
}
