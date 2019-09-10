package dev.imabad.mceventmanager.core.database.player;


import dev.imabad.mceventmanager.core.EventCore;
import dev.imabad.mceventmanager.core.api.objects.EventRank;
import dev.imabad.mceventmanager.core.database.MongoDatabase;
import dev.morphia.query.Sort;
import java.util.List;

public class RankDatabase {

    private static RankDatabase instance;

    public static RankDatabase getInstance(){
        if(!EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).isConnected()){
            EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).connect();
        }
        if(instance == null){
            instance = new RankDatabase();
        }
        return instance;
    }

    public EventRank getLowestRank(){
        return EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getDatastore().createQuery(EventRank.class).order(Sort.descending("power")).first();
    }

    public List<EventRank> getRanks(){
        return EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getDatastore().createQuery(EventRank.class).order(Sort.descending("power")).asList();
    }

    public void saveRank(EventRank rank){
        EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getDatastore().save(rank);
    }

}
