package dev.imabad.mceventmanager.core;

import dev.imabad.mceventmanager.core.api.objects.EventPlayer;
import dev.imabad.mceventmanager.core.api.objects.EventRank;

import java.io.File;
import java.util.Collections;
import java.util.UUID;

public class EventCoreMain {

    public static void main(String[] args){
        File folder = new File("mceventsuite");
        new EventCore(folder);
        if(EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase().isConnected()){
            if(EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase().getRanks().size() == 0){
                EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase().saveRank(new EventRank(100, "Admin", "", "", Collections.emptyList()));
            }
            EventPlayer eventPlayer = EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase().getOrCreatePlayer(UUID.fromString("e1949d31-4f97-423a-8229-690fd6756b1c"), "Rushmead");
            System.out.println("Player: " + eventPlayer.getLastUsername());
            System.out.println("Rank: " + eventPlayer.getRank().getName());
        } else {
            System.out.println("Could not connect to database");
        }
    }
}
