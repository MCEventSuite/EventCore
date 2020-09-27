package dev.imabad.mceventsuite.core.modules.discord;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.audit.db.AuditLogEntry;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;

public class DiscordLinkDAO extends DAO {

    public DiscordLinkDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public EventPlayer getPlayerFromDiscord(String discordID){
        try(Session session = mySQLDatabase.getSession()){
            Query<EventPlayer> q= session.createQuery("select d.player FROM DiscordLink d LEFT JOIN FETCH d.player.permissions e WHERE d.discordID = :discordID", EventPlayer.class);
            q.setParameter("discordID", discordID);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public DiscordLink getPlayerDiscordLink(EventPlayer player){
        try(Session session = mySQLDatabase.getSession()){
            Query<DiscordLink> q= session.createQuery("select d FROM DiscordLink d LEFT JOIN FETCH d.player.permissions e WHERE d.player.UUID = :uuid", DiscordLink.class);
            q.setParameter("uuid", player.getUUID());
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public void saveDiscordLink(DiscordLink auditLogEntry){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(auditLogEntry);
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
