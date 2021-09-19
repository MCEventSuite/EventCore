package dev.imabad.mceventsuite.core.modules.announcements;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncement;
import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncementDAO;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;

import java.util.*;

public class AnnouncementsModule extends Module {

    private MySQLDatabase mySQLDatabase;
    private ScheduledAnnouncementDAO dao;
    private PriorityQueue<ScheduledAnnouncement> announcements = new PriorityQueue<>();

    @Override
    public String getName() {
        return "announcements";
    }

    @Override
    public void onEnable() {
        mySQLDatabase = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase();
        EventCore.getInstance().getEventRegistry().registerListener(MySQLLoadedEvent.class, mySQLLoadedEvent -> {
            dao = new ScheduledAnnouncementDAO(mySQLDatabase);
            mySQLDatabase.registerDAOs(dao);
            reloadAnnouncements();
        });
    }

    @Override
    public void onDisable() {
        announcements.clear();
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Arrays.asList(MySQLModule.class);
    }

    public PriorityQueue<ScheduledAnnouncement> getAnnouncements() {
        return this.announcements;
    }

    public void reloadAnnouncements() {
        announcements.clear();

        for (ScheduledAnnouncement announcement : dao.getAllScheduledAnnouncements()) {
            // Skip over any missed broadcasts
            if (announcement.getNextRun() < System.currentTimeMillis()) {
                double missedAnnouncements = (System.currentTimeMillis() - announcement.getNextRun())
                        / announcement.getInterval();
                announcement.setNextRun(announcement.getNextRun()
                        + Math.round(Math.ceil(missedAnnouncements) * announcement.getInterval()));
                dao.saveOrUpdateScheduledAnnouncement(announcement);
            }

            announcements.add(announcement);
        }

        if (announcements.peek() != null) {
            System.out.println("Next announcement scheduled for: " + new Date(announcements.peek().getNextRun()));
        }
    }

    protected ScheduledAnnouncementDAO getDao() {
        return this.dao;
    }

}
