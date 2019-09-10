package dev.imabad.mceventmanager.core;

import dev.imabad.mceventmanager.core.api.objects.EventPlayer;
import dev.imabad.mceventmanager.core.api.objects.EventRank;
import dev.imabad.mceventmanager.core.database.MongoDatabase;
import dev.imabad.mceventmanager.core.database.player.PlayerDatabase;
import dev.imabad.mceventmanager.core.database.player.RankDatabase;
import java.io.File;
import java.util.Collections;
import java.util.UUID;

public class EventCoreMain {

    public static void main(String[] args){
        File folder = new File("mceventsuite");
        new EventCore(folder);
        if(EventCore.getInstance().getDatabaseRegistry().getDatabase(MongoDatabase.class).getConfig().getHostname() != null){
            if(RankDatabase.getInstance().getRanks().size() == 0){
                RankDatabase.getInstance().saveRank(new EventRank(100, "Admin", "", "", Collections.emptyList()));
            }
            EventPlayer eventPlayer = PlayerDatabase.getInstance().getOrCreatePlayer(UUID.fromString("e1949d31-4f97-423a-8229-690fd6756b1c"), "Rushmead");
            System.out.println("Player: " + eventPlayer.getLastUsername());
            System.out.println("Rank: " + eventPlayer.getRank().getName());
        } else {
            System.out.println("Could not connect to database");
        }
    }
}
