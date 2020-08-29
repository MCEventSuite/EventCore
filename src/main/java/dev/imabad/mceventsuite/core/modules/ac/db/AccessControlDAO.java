package dev.imabad.mceventsuite.core.modules.ac.db;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

public class AccessControlDAO extends DAO {

    public AccessControlDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public List<AccessControlSetting> getAccessControlSettings(){
        try (Session session = mySQLDatabase.getSession()) {
            Query query = session.createQuery("select a from AccessControlSetting a");
            return (List<AccessControlSetting>) query.list();
        }
    }
}
