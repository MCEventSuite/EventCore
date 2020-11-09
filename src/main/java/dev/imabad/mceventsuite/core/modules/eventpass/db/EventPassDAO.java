package dev.imabad.mceventsuite.core.modules.eventpass.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

public class EventPassDAO extends DAO {

    public EventPassDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    /**
     * Get or create an event pass player
     * @param player    The player to find or create one for
     * @return          An EventPassPlayer
     */
    public EventPassPlayer getOrCreateEventPass(EventPlayer player){
        EventPassPlayer eventPassPlayer = getPlayerEventPass(player);
        if(eventPassPlayer == null) {
            eventPassPlayer = new EventPassPlayer(player, player.getRank().getInitialEventPassXP());
            saveEventPassPlayer(eventPassPlayer);
        }
        return eventPassPlayer;
    }

    public EventPassPlayer getPlayerEventPass(EventPlayer player){
        try (Session session = mySQLDatabase.getSession()) {
            Query<EventPassPlayer> q= session.createQuery("select p FROM EventPassPlayer p WHERE p.player = :player", EventPassPlayer.class);
            q.setParameter("player", player);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public void saveEventPassPlayer(EventPassPlayer eventPassPlayer){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(eventPassPlayer);
            tx.commit();
        }
        catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
    }

    public List<EventPassReward> getRewards(){
        Session session = mySQLDatabase.getSession();
        try {
            return session.createQuery("select r from EventPassReward r", EventPassReward.class).list();
        } finally {
            session.close();
        }
    }
}
