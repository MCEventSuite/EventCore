package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventBooth;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

public class BoothDAO extends DAO {

    public BoothDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public List<EventBooth> getBooths(){
        Session session = mySQLDatabase.getSession();
        try {
            return session.createQuery("select r from EventBooth r", EventBooth.class).list();
        } finally {
            session.close();
        }
    }

    public List<EventBooth> getPlayerBooths(EventPlayer player){
        try(Session session = mySQLDatabase.getSession()){
            Query<EventBooth> boothQuery = session.createQuery("select r from EventBooth r where :player MEMBER OF r.members OR r.owner = :player", EventBooth.class);
            boothQuery.setParameter("player", player);
            return boothQuery.getResultList();
        }
    }

    public void saveBooth(EventBooth booth){
        Transaction tx = null;
        try (Session session = mySQLDatabase.getSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(booth);
            tx.commit();
        } catch (RuntimeException e) {
            assert tx != null;
            tx.rollback();
        }
    }
}
