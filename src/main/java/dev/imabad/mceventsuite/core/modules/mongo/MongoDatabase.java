package dev.imabad.mceventsuite.core.modules.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.sun.jdi.event.EventSet;
import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.database.DatabaseProvider;
import dev.imabad.mceventsuite.core.api.database.DatabaseType;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.api.objects.EventSetting;
import dev.imabad.mceventsuite.core.config.database.MongoConfig;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Sort;

import java.util.List;
import java.util.UUID;

public class MongoDatabase extends DatabaseProvider {

    private boolean connected = false;
    private MongoConfig mongoConfig;
    private Morphia morphia;
    private MongoClient mongoClient;
    private Datastore datastore;

    public MongoDatabase(MongoConfig mongoConfig){
        this.mongoConfig = mongoConfig;
    }

    @Override
    public String getName() {
        return "mongodb";
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
        EventCore.getInstance().getEventRegistry().handleEvent(new MongoLoadedEvent());
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

    public EventPlayer getPlayer(String username) {
       return datastore.createQuery(EventPlayer.class).field("lastUsername").equalIgnoreCase(username).first();
    }

    public EventPlayer getPlayer(UUID uuid) {
        return datastore.createQuery(EventPlayer.class).field("uuid").equal(uuid).first();
    }

    public void savePlayer(EventPlayer player) {
        save(player);
    }

    public List<EventPlayer> getPlayers() {
        return datastore.createQuery(EventPlayer.class).asList();
    }

    public EventRank getLowestRank() {
        return datastore.createQuery(EventRank.class).order(Sort.ascending("power")).first();
    }

    public List<EventRank> getRanks() {
        return datastore.createQuery(EventRank.class).order(Sort.descending("power")).asList();
    }

    public void saveRank(EventRank eventRank) {
        save(eventRank);
    }

    public EventSetting getSetting(EventSetting setting){
        EventSetting settingInDB = datastore.createQuery(EventSetting.class).field("group").equalIgnoreCase(setting.getGroup()).field("name").equalIgnoreCase(setting.getName()).first();
        return settingInDB == null ? setting : settingInDB;
    }

    public void saveSetting(EventSetting setting){
        datastore.save(setting);
    }

    public <T> T get(Class<T> object) {
        return datastore.createQuery(object).first();
    }

    public void save(Object object) {
        datastore.save(object);
    }
}
