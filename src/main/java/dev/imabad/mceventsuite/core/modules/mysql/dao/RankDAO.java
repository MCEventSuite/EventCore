package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

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

    public Optional<EventRank> getLowestRank(){
        if(ranks == null){
            getRanks(true);
        }
        if(ranks.size() < 1){
            return Optional.of(new EventRank(0, "Default", "", "", Collections.emptyList()));
        }
        return ranks.stream().min(Comparator.comparingInt(EventRank::getPower));
    }
}
