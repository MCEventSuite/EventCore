package dev.imabad.mceventmanager.core.registries;

import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.DatabaseProvider;
import dev.imabad.mceventmanager.core.api.IRegistry;
import dev.imabad.mceventmanager.core.api.exceptions.NotRegisteredException;
import java.util.HashMap;

public class DatabaseRegistry implements IRegistry {

    private boolean loaded = true;
    private HashMap<Class<?extends DatabaseProvider>, DatabaseProvider> databases;

    public DatabaseRegistry(){
        databases = new HashMap<>();
    }

    public <T extends DatabaseProvider> void register(Class<T> databaseProviderClazz, DatabaseProvider databaseProvider){
        databases.put(databaseProviderClazz, databaseProvider);
        EventCore.getInstance().getConfigRegistry().registerConfig(databaseProvider);
    }

    public void connectAll(){
        databases.values().stream().filter(databaseProvider -> !databaseProvider.isConnected()).forEach(DatabaseProvider::connect);
    }

    public void disconnectAll(){
        databases.values().stream().filter(DatabaseProvider::isConnected).forEach(DatabaseProvider::disconnect);
    }

    public <T extends DatabaseProvider> T getDatabase(Class<T> clazz){
        if(!databases.containsKey(clazz)){
            throw new NotRegisteredException(this, clazz.getName());
        }
        return clazz.cast(databases.get(clazz));
    }

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
