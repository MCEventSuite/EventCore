package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventSetting;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM event_settings WHERE event_settings.group = ? AND event_settings.name = ?");
            preparedStatement.setString(1, group);
            preparedStatement.setString(2, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new EventSetting(resultSet.getString("group"), resultSet.getString("name"), resultSet.getString("value"), resultSet.getString("permission"));
            } else {
                return null;
            }
        }catch(SQLException e){
            //TODO: Better logging
            e.printStackTrace();
        }
        return null;
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
        EventSetting dbSetting = getSetting(eventSetting.getGroup(), eventSetting.getName());
        if(dbSetting == null){
            return eventSetting;
        }
        return dbSetting;
    }

    /**
     * Saves a new setting to the database
     * @param eventSetting  The setting to save
     * @see                 EventSetting
     */
    public boolean saveNewSetting(EventSetting eventSetting){
        if(settingExists(eventSetting)){
            return saveSetting(eventSetting);
        }
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO event_settings VALUES (?, ?, ?, ?);");
            insertStatement.setString(1, eventSetting.getName());
            insertStatement.setString(2, eventSetting.getGroup());
            insertStatement.setString(3, eventSetting.getPermission());
            insertStatement.setString(4, eventSetting.getValueJSON());
            return insertStatement.executeUpdate() != 0;
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
        return false;
    }

    public boolean saveSetting(EventSetting eventSetting){
        if(!settingExists(eventSetting)){
            return saveNewSetting(eventSetting);
        }
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement insertStatement = connection.prepareStatement("UPDATE event_settings SET value = ? WHERE group = ? AND name = ?;");
            insertStatement.setString(1, eventSetting.getValueJSON());
            insertStatement.setString(2, eventSetting.getGroup());
            insertStatement.setString(3, eventSetting.getName());
            return insertStatement.executeUpdate() != 0;
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
        return false;
    }

    public boolean settingExists(EventSetting setting){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT value FROM event_settings WHERE event_settings.group = ? AND event_settings.name = ?");
            preparedStatement.setString(1, setting.getGroup());
            preparedStatement.setString(2, setting.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch(SQLException e){
            //TODO: Better logging
            e.printStackTrace();
        }
        return false;
    }

}
