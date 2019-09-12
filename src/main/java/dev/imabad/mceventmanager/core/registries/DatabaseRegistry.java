package dev.imabad.mceventmanager.core.registries;

import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.database.DatabaseProvider;
import dev.imabad.mceventmanager.core.api.IRegistry;
import dev.imabad.mceventmanager.core.api.database.DatabaseType;
import dev.imabad.mceventmanager.core.api.database.IDatabase;
import dev.imabad.mceventmanager.core.api.database.IPersistentDatabase;
import dev.imabad.mceventmanager.core.api.exceptions.NotRegisteredException;
import java.util.HashMap;

public class DatabaseRegistry implements IRegistry {

    private boolean loaded = true;
    private HashMap<DatabaseType, DatabaseProvider> databases;

    public DatabaseRegistry(){
        databases = new HashMap<>();
    }

    public <T extends DatabaseProvider> void register(DatabaseProvider databaseProvider){
        databases.put(databaseProvider.getType(), databaseProvider);
        EventCore.getInstance().getConfigRegistry().registerConfig(databaseProvider);
    }

    public void connectAll(){
        databases.values().stream().filter(databaseProvider -> !databaseProvider.isConnected()).forEach(DatabaseProvider::connect);
    }

    public void disconnectAll(){
        databases.values().stream().filter(DatabaseProvider::isConnected).forEach(DatabaseProvider::disconnect);
    }

    public IPersistentDatabase getPersistentDatabase(){
        return (IPersistentDatabase) databases.get(DatabaseType.PERSISTENT);
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
