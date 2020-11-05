package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventBooth;
import dev.imabad.mceventsuite.core.api.objects.EventBoothPlot;
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
import java.util.UUID;

public class BoothDAO extends DAO {

    public BoothDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public List<EventBooth> getBooths(){
        Session session = mySQLDatabase.getSession();
        try {
            return session.createQuery("select r from EventBooth r LEFT JOIN FETCH r.owner.permissions e", EventBooth.class).list();
        } finally {
            session.close();
        }
    }

    public List<EventBoothPlot> getPlots(){
        Session session = mySQLDatabase.getSession();
        try {
            return session.createQuery("select r from EventBoothPlot r LEFT JOIN FETCH r.booth b", EventBoothPlot.class).list();
        } finally {
            session.close();
        }
    }

    public EventBooth getBoothFromID(UUID uuid){
        try(Session session = mySQLDatabase.getSession()){
            Query<EventBooth> boothQuery = session.createQuery("select r from EventBooth r LEFT JOIN FETCH r.owner.permissions e where r.id = :uuid ", EventBooth.class);
            boothQuery.setParameter("uuid", uuid.toString());
            return boothQuery.getSingleResult();
        }
    }

    public List<EventBooth> getPlayerBooths(EventPlayer player){
        try(Session session = mySQLDatabase.getSession()){
            Query<EventBooth> boothQuery = session.createQuery("select r from EventBooth r where :player MEMBER OF r.members OR r.owner = :player", EventBooth.class);
            boothQuery.setParameter("player", player);
            return boothQuery.getResultList();
        }
    }

    public EventBooth getBoothFromPlotID(String type, String plotID){
        try(Session session = mySQLDatabase.getSession()){
            Query<EventBooth> boothQuery = session.createQuery("select r from EventBooth r where r.boothType = :type and r.plotID = :plotID", EventBooth.class);
            boothQuery.setParameter("type", type);
            boothQuery.setParameter("plotID", plotID);
            return boothQuery.getSingleResult();
        }catch(Exception e){
            return null;
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

    public void saveBoothPlot(EventBoothPlot plot){
        Transaction tx = null;
        try (Session session = mySQLDatabase.getSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(plot);
            tx.commit();
        } catch (RuntimeException e) {
            assert tx != null;
            tx.rollback();
        }
    }
}
