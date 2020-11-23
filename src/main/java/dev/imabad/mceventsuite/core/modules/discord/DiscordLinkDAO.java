package dev.imabad.mceventsuite.core.modules.discord;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import redis.clients.jedis.Transaction;

public class DiscordLinkDAO extends DAO {

    public DiscordLinkDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public EventPlayer getPlayerFromDiscord(String discordID){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement query = connection.prepareStatement("SELECT * FROM players WHERE players.uuid = (SELECT player FROM discord_links WHERE discordID = ?) LIMIT 1;");
            query.setString(1, discordID);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } catch(SQLException ignored){
            return null;
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
