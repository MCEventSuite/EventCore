package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mongo.MongoDatabase;
import dev.imabad.mceventsuite.core.modules.mongo.MongoLoadedEvent;
import dev.imabad.mceventsuite.core.modules.mongo.MongoModule;

import java.io.File;
import java.util.Collections;
import java.util.UUID;

public class EventCoreMain {

    public static void main(String[] args) throws InterruptedException {
        File folder = new File("mceventsuite");
        new EventCore(folder);
        EventCore.getInstance().getEventRegistry().registerListener(MongoLoadedEvent.class, (event) -> {
            MongoDatabase mongoDatabase = EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase();
            if(mongoDatabase.getRanks().size() == 0){
                mongoDatabase.saveRank(new EventRank(100, "Admin", "", "", Collections.emptyList()));
            }
            EventPlayer eventPlayer = mongoDatabase.getOrCreatePlayer(UUID.fromString("e1949d31-4f97-423a-8229-690fd6756b1c"), "Rushmead");
            System.out.println("Player: " + eventPlayer.getLastUsername());
            System.out.println("Rank: " + eventPlayer.getRank().getName());
        });
        Thread.sleep(1000 * 10);
    }
}
