package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import redis.clients.jedis.Transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerDAO extends DAO {

    public PlayerDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);

    }

    @Override
    public void setup() {
        try(Connection connection = mySQLDatabase.getConnection();
            Statement statement = connection.createStatement();){
            statement.executeUpdate("create table if not exists players ( uuid varchar(255) not null primary key, last_username varchar(255) null, properties varchar(255) null, rank_id int null, constraint FKdd8qslf7ue2b3neu4bojbosix foreign key (rank_id) references ranks (id) );");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all players
     * @return  A list of players
     * @see     EventPlayer
     */
    public List<EventPlayer> getPlayers(){
        try (Connection connection = mySQLDatabase.getConnection();
             Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery("select * FROM players;");
            Query<EventPlayer> q= session.createQuery("select p FROM EventPlayer p LEFT JOIN FETCH p.permissions e", EventPlayer.class);
            try {
                return q.getResultList();
            } catch (NoResultException e) {
                return Collections.emptyList();
            }
        }catch(SQLException e){
            return Collections.emptyList();
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
        try {
            EventPlayer eventPlayer = getPlayer(uuid);
            if (eventPlayer == null) {
                eventPlayer = getPlayer(username);
                if (eventPlayer == null) {
                    eventPlayer = new EventPlayer(uuid, username);
                    savePlayer(eventPlayer);
                }
            }
            return eventPlayer;
        } catch(Exception e){
            return null;
        }
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
            Query<EventPlayer> q= session.createQuery("select p FROM EventPlayer p LEFT JOIN FETCH p.permissions e WHERE p.UUID = :uuid", EventPlayer.class);
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
            Query<EventPlayer> q = session.createQuery("select p FROM EventPlayer p LEFT JOIN FETCH p.permissions e WHERE p.lastUsername = :username",
                    EventPlayer.class);
            q.setParameter("username", username);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                e.printStackTrace();
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
            session.saveOrUpdate(player);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
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
