package dev.imabad.mceventsuite.core.modules.announcements.db;

import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ScheduledAnnouncementDAO extends DAO {

    public ScheduledAnnouncementDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public List<ScheduledAnnouncement> getAllScheduledAnnouncements(){
        try (Session session = mySQLDatabase.getSession()) {
            Query<ScheduledAnnouncement> query = session.createQuery("select a from ScheduledAnnouncement a", ScheduledAnnouncement.class);
            return query.getResultList();
        }
    }

    public void saveOrUpdateScheduledAnnouncement(ScheduledAnnouncement scheduledAnnouncement){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(scheduledAnnouncement);
            tx.commit(); // Flush happens automatically
        }
        catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }

}
