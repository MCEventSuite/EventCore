package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankDAO extends DAO {

    public RankDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public List<EventRank> getRanks(){
        Session session = mySQLDatabase.getSession();
        try {
            List<EventRank> ranks = session.createQuery("select r from EventRank r", EventRank.class).list();
            return ranks;
        } finally {
            session.close();
        }
    }

    public void saveRank(EventRank rank){
        Transaction tx = null;
        try (Session session = mySQLDatabase.getSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(rank);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            assert tx != null;
            tx.rollback();
        }
    }

    public EventRank getLowestRank(){
        Session session = mySQLDatabase.getSession();
        try {
            return session.createQuery("select r FROM EventRank r order by r.power asc", EventRank.class).setMaxResults(1).getSingleResult();
        }catch(NoResultException e){
            return new EventRank(0, "Default", "", "", Collections.emptyList());
        } finally {
            session.close();
        }
    }
}
