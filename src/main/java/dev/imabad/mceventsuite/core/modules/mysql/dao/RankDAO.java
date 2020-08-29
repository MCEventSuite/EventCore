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
import java.util.Optional;

public class RankDAO extends DAO {

    private List<EventRank> ranks;

    public RankDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public Optional<EventRank> getRankByName(String name){
        if(ranks == null){
            getRanks(true);
        }
        return ranks.stream().filter(eventRank -> eventRank.getName().equalsIgnoreCase(name)).findFirst();
    }

    public List<EventRank> getRanks(){
        if(ranks != null){
            return ranks;
        }
        Session session = mySQLDatabase.getSession();
        try {
            ranks = session.createQuery("select r from EventRank r", EventRank.class).list();
            return ranks;
        } finally {
            session.close();
        }
    }

    public List<EventRank> getRanks(boolean refresh){
        if(!refresh){
            return getRanks();
        }
        Session session = mySQLDatabase.getSession();
        try {
            ranks = session.createQuery("select r from EventRank r", EventRank.class).list();
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
