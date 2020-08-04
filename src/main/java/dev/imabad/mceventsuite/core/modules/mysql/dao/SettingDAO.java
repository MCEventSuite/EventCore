package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventSetting;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.UUID;

public class SettingDAO extends DAO {

    public SettingDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    /**
     * Gets a setting based on it's group and name
     * Returns null if no setting exists
     *
     * @param group - Name of the group that the setting is apart of
     * @param name  - Name of the setting
     * @return      - An instance of EventSetting or null
     * @see         EventSetting
     */
    public EventSetting getSetting(String group, String name){
        try(Session session = mySQLDatabase.getSession()) {
            Query<EventSetting> query = session.createQuery("SELECT s FROM EventSetting s WHERE s.group = :group AND s.name = :name", EventSetting.class).setParameter("group", group).setParameter("name", name);
            return query.getSingleResult();
        }
    }

    /**
     * Get a setting based on an existing setting object
     * If no setting is found the original setting is returned
     *
     * @param eventSetting  - The existing setting to find
     * @return              - An instance of EventSetting
     * @see                 EventSetting
     */
    public EventSetting getSetting(EventSetting eventSetting){
        try(Session session = mySQLDatabase.getSession()) {
            Query<EventSetting> query = session.createQuery("SELECT s FROM EventSetting s WHERE s.group = :group AND s.name = :name", EventSetting.class).setParameter("group", eventSetting.getGroup()).setParameter("name", eventSetting.getName());
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return eventSetting;
            }
        }
    }

    /**
     * Saves a setting to the database
     * @param eventSetting  The setting to save
     * @see                 EventSetting
     */
    public void saveSetting(EventSetting eventSetting){
        try(Session session = mySQLDatabase.getSession()) {
            session.save(eventSetting);
        }
    }

}
