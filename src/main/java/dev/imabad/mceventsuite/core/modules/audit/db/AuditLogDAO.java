package dev.imabad.mceventsuite.core.modules.audit.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.ac.db.PlayerBan;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

public class AuditLogDAO extends DAO {

    public AuditLogDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public void saveAuditLog(AuditLogEntry auditLogEntry){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(auditLogEntry);
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

    public List<AuditLogEntry> getPlayerHistory(EventPlayer player){
        try (Session session = mySQLDatabase.getSession()) {
            Query<AuditLogEntry> q = session.createQuery("select p FROM AuditLogEntry p WHERE p.actionedPlayer = :player", AuditLogEntry.class);
            q.setParameter("player", player);
            try {
                return q.getResultList();
            } catch (NoResultException e) {
                return Collections.emptyList();
            }
        }
    }
}
