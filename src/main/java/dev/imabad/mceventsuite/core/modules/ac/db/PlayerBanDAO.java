package dev.imabad.mceventsuite.core.modules.ac.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class PlayerBanDAO extends DAO {
    public PlayerBanDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public PlayerBan getPlayerActiveBan(EventPlayer player){
        long currentTime = System.currentTimeMillis();
        try (Session session = mySQLDatabase.getSession()) {
            Query<PlayerBan> query = session.createQuery("select a from PlayerBan a WHERE a.player = :player and (a.expires = -1 or a.expires > :currentTime) order by a.banDate desc", PlayerBan.class);
            query.setParameter("player", player);
            query.setParameter("currentTime", currentTime);
            if(query.getResultList().size() > 0){
                return query.getResultList().get(0);
            }
            return null;
        }
    }

    public List<PlayerBan> getAllPlayerBans(EventPlayer player){
        try (Session session = mySQLDatabase.getSession()) {
            Query<PlayerBan> query = session.createQuery("select a from PlayerBan a WHERE a.player = :player order by a.banDate desc", PlayerBan.class);
            query.setParameter("player", player);
            return query.getResultList();
        }
    }

    public List<PlayerBan> getAllActiveBans(){
        long currentTime = System.currentTimeMillis();
        try (Session session = mySQLDatabase.getSession()) {
            Query<PlayerBan> query = session.createQuery("select a from PlayerBan a WHERE a.expires = -1 or a.expires > :currentTime order by a.banDate desc", PlayerBan.class);
            query.setParameter("currentTime", currentTime);
            return query.getResultList();
        }
    }

    public void saveBan(PlayerBan playerBan){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(playerBan);
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
