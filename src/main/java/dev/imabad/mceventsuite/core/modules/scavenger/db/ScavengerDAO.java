package dev.imabad.mceventsuite.core.modules.scavenger.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.TypeDef;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScavengerDAO extends DAO {

    public ScavengerDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public List<ScavengerLocation> getLocations(){
        Session session = mySQLDatabase.getSession();
        try {
            Query<ScavengerLocation> query = session.createQuery("select r from ScavengerLocation r", ScavengerLocation.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    public Set<ScavengerLocation> getPlayerFoundLocations(EventPlayer eventPlayer){
        Session session = mySQLDatabase.getSession();
        try {
            Query<ScavengerHuntPlayer> query = session.createQuery("select r from ScavengerHuntPlayer r LEFT JOIN FETCH r.foundLocations WHERE r.player = :player", ScavengerHuntPlayer.class);
            query.setParameter("player", eventPlayer);
            return query.getSingleResult().getFoundLocations();
        } finally {
            session.close();
        }
    }

    public ScavengerHuntPlayer getOrCreateScavengerHuntPlayer(EventPlayer player){
        ScavengerHuntPlayer scavengerHuntPlayer = getScavengerPlayer(player);
        if(scavengerHuntPlayer == null) {
            scavengerHuntPlayer = new ScavengerHuntPlayer(player);
            saveScavengerHuntPlayer(scavengerHuntPlayer);
        }
        return scavengerHuntPlayer;
    }

    public void saveScavengerHuntPlayer(ScavengerHuntPlayer scavengerHuntPlayer){
        Session session = mySQLDatabase.getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(scavengerHuntPlayer);
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

    public ScavengerHuntPlayer getScavengerPlayer(EventPlayer player){
        try (Session session = mySQLDatabase.getSession()) {
            Query<ScavengerHuntPlayer> q= session.createQuery("select p FROM ScavengerHuntPlayer p WHERE p.player = :player", ScavengerHuntPlayer.class);
            q.setParameter("player", player);
            try {
                return q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }
    }

}
