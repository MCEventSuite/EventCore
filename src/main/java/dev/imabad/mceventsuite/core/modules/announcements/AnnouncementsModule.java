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
    private List<ScheduledAnnouncement> announcements = new ArrayList<>();

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

    public List<ScheduledAnnouncement> getAnnouncements() {
        return this.announcements;
    }

    public void reloadAnnouncements() {
        announcements.clear();

        for (ScheduledAnnouncement announcement : dao.getAllScheduledAnnouncements()) {
            announcements.add(announcement);
        }
    }

    protected ScheduledAnnouncementDAO getDao() {
        return this.dao;
    }

}
