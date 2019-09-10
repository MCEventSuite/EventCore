package dev.imabad.mceventmanager.core.database.player;

import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.objects.EventPlayer;
import dev.imabad.mceventmanager.core.database.MongoDatabase;
import java.util.UUID;

public class PlayerDatabase {

    private static PlayerDatabase instance;

    public static PlayerDatabase getInstance(){
        if(!EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).isConnected()){
            EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).connect();
        }
        if(instance == null){
            instance = new PlayerDatabase();
        }
        return instance;
    }

    public EventPlayer getOrCreatePlayer(UUID uuid, String username){
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

    public EventPlayer getPlayer(UUID uuid){
        return EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getDatastore().createQuery(EventPlayer.class).field("uuid").equalIgnoreCase(uuid).first();
    }

    public EventPlayer getPlayer(String username){
        return EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getDatastore().createQuery(EventPlayer.class).field("lastUsername").equalIgnoreCase(username).first();
    }

    public void savePlayer(EventPlayer eventPlayer){
        EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getDatastore().save(eventPlayer);
    }
}
