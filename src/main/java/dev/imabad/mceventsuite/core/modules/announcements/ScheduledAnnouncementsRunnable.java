package dev.imabad.mceventsuite.core.modules.announcements;

import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncement;

import java.util.List;
import java.util.PriorityQueue;

public abstract class ScheduledAnnouncementsRunnable implements Runnable {

    private AnnouncementsModule module;
    int currentAnnouncement;

    public ScheduledAnnouncementsRunnable(AnnouncementsModule module) {
        this.module = module;
    }

    public abstract void broadcastMessage(String message);

    @Override
    public void run() {
        List<ScheduledAnnouncement> announcements = module.getAnnouncements();
        if (announcements.size() == 0) return;
        if(currentAnnouncement >= announcements.size())
            currentAnnouncement = 0;

        ScheduledAnnouncement announcement = announcements.get(currentAnnouncement);
        this.broadcastMessage(announcement.getMessage());
        currentAnnouncement++;

    }

}
