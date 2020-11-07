package dev.imabad.mceventsuite.core.modules.announcements;

import dev.imabad.mceventsuite.core.modules.announcements.db.ScheduledAnnouncement;

import java.util.PriorityQueue;

public abstract class ScheduledAnnouncementsRunnable implements Runnable {

    private AnnouncementsModule module;

    public ScheduledAnnouncementsRunnable(AnnouncementsModule module) {
        this.module = module;
    }

    public abstract void broadcastMessage(String message);

    @Override
    public void run() {
        PriorityQueue<ScheduledAnnouncement> announcements = module.getAnnouncements();

        if (announcements.size() == 0) return;

        while (announcements.peek().getNextRun() <= System.currentTimeMillis()) {
            ScheduledAnnouncement announcement = announcements.poll();

            this.broadcastMessage(announcement.getMessage());

            announcement.setNextRun(System.currentTimeMillis() + announcement.getInterval());
            module.getDao().saveOrUpdateScheduledAnnouncement(announcement);
            announcements.add(announcement);
        }
    }

}
