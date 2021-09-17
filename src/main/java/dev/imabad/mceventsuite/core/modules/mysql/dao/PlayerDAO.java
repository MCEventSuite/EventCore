package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventPlayerYear;
import dev.imabad.mceventsuite.core.api.objects.EventYear;
import dev.imabad.mceventsuite.core.modules.eventpass.db.EventPassUnlockedReward;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerDAO extends DAO {

    public PlayerDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public EventYear getCurrentYear(){
        try (Session session = mySQLDatabase.getSession()) {
            Query<EventYear> q= session.createQuery("select p FROM EventYear p Where p.year = :year", EventYear.class);
            q.setParameter("year", EventCore.getInstance().getConfig().getCurrentYearAsInt());
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    public void saveEventPlayerYear(EventPlayerYear eventPlayerYear){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(eventPlayerYear);
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

    /**
     * Get all players
     * @return  A list of players
     * @see     EventPlayer
     */
    public List<EventPlayer> getPlayers(){
        try (Session session = mySQLDatabase.getSession()) {
            Query<EventPlayer> q= session.createQuery("select p FROM EventPlayer p LEFT JOIN FETCH p.permissions LEFT JOIN FETCH p.attendance", EventPlayer.class);
            try {
                return q.getResultList();
            } catch (NoResultException e) {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Get or create a player using a UUID or username
     * If a player does not exist a new one is created using the provided UUID and username.
     *
     * @param uuid      - UUID of the player to find or create
     * @param username  - Username of the player to find or create
     * @return          - An instance of EventPlayer
     * @see             EventPlayer
     */
    public EventPlayer getOrCreatePlayer(UUID uuid, String username){
        EventPlayer eventPlayer = getPlayer(uuid);
        if (eventPlayer == null) {
            eventPlayer = getPlayer(username);
            if (eventPlayer == null) {
                eventPlayer = new EventPlayer(uuid, username);
                savePlayer(eventPlayer);
            } else {
                if(!eventPlayer.getUUID().equals(uuid)){
                    eventPlayer.setUUID(uuid);
                }
            }
        }
        if(!eventPlayer.getLastUsername().equals(username)){
            eventPlayer.setLastUsername(username);
            saveOrUpdatePlayer(eventPlayer);
        }
        return eventPlayer;
    }

    /**
     * Get an EventPlayer by their UUID
     * This is the preferred method
     *
     * @param uuid - UUID of the player to find
     * @return     - An instance of EventPlayer or null
     * @see        EventPlayer
     */
    public EventPlayer getPlayer(UUID uuid){
        try (Session session = mySQLDatabase.getSession()) {
            Query<EventPlayer> q= session.createQuery("select p FROM EventPlayer p LEFT JOIN FETCH p.permissions LEFT JOIN FETCH p.attendance WHERE p.UUID = :uuid", EventPlayer.class);
            q.setParameter("uuid", uuid);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    /**
     * Get an EventPlayer by their username
     * Avoid this where possible.
     *
     * @param username  - Username of the player to find
     * @return          - the player with that username or null
     * @see             EventPlayer
     */
    public EventPlayer getPlayer(String username){
        try(Session session = mySQLDatabase.getSession()) {
            System.out.println("Checking for player with username: " + username);
            Query<EventPlayer> q = session.createQuery("select p FROM EventPlayer p LEFT JOIN FETCH p.permissions LEFT JOIN FETCH p.attendance WHERE p.lastUsername = :username ",
                    EventPlayer.class);
            q.setParameter("username", username);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

    /**
     * Saves an EventPlayer to the database
     * You should avoid this where possible and ask the central service to do this for you
     * @param player - The EventPlayer you wish to save
     */
    @Deprecated
    public void saveOrUpdatePlayer(EventPlayer player){
        Transaction tx = null;
        try (Session session = mySQLDatabase.getSession()) {
            tx = session.beginTransaction();
            player = (EventPlayer) session.merge("dev.imabad.mceventsuite.core.api.objects.EventPlayer", player);
            session.saveOrUpdate(player);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Error in saveOrUpdate player, attempting rollback...");

            assert tx != null;
            tx.rollback();
        }
    }

    /**
     * Saves an EventPlayer to the database
     * You should avoid this where possible and ask the central service to do this for you
     * @param player - The EventPlayer you wish to save
     */
    @Deprecated
    public void savePlayer(EventPlayer player){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(player);
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
