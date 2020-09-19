package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.PlayerDAO;
import dev.imabad.mceventsuite.core.modules.mysql.dao.RankDAO;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;

import java.io.File;
import java.util.Collections;
import java.util.UUID;

public class EventCoreMain {

    public static void main(String[] args) throws InterruptedException {
        File folder = new File("mceventsuite");
        new EventCore(folder);
        EventCore.getInstance().getEventRegistry().registerListener(MySQLLoadedEvent.class, (event) -> {
            MySQLDatabase mySQLDatabase = event.getMySQLDatabase();
            if(mySQLDatabase.getDAO(RankDAO.class).getRanks().size() == 0){
                mySQLDatabase.getDAO(RankDAO.class).saveRank(new EventRank(100, "Admin", "[Admin]", "", Collections.emptyList()));
            }

            EventPlayer eventPlayer = mySQLDatabase.getDAO(PlayerDAO.class).getOrCreatePlayer(UUID.fromString("e1949d31-4f97-423a-8229-690fd6756b1c"), "Rushmead");
            System.out.println("Player: " + eventPlayer.getLastUsername());
            System.out.println("Rank: " + eventPlayer.getRank().getName());
            System.out.println("Coins: " + eventPlayer.getIntProperty("coins"));
            eventPlayer.setProperty("coins", eventPlayer.getIntProperty("coins") + 5);
            mySQLDatabase.getDAO(PlayerDAO.class).saveOrUpdatePlayer(eventPlayer);
        });
        Thread.sleep(1000 * 10);
        EventCore.getInstance().shutdown();
    }
}
