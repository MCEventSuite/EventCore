package dev.imabad.mceventsuite.core.modules.mysql;

import dev.imabad.mceventsuite.core.EventCore;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLTables {

    private static MySQLDatabase mySQLDatabase = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase();

    static void createTables(){
        createPlayerTable();
        createRankTable();
        createPermissionsTable();
        createSettingsTable();
    }

    private static void createPlayerTable(){
        try(Connection connection = mySQLDatabase.getHikari().getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS players(player_id INT NOT NULL AUTO_INCREMENT, uuid varchar(36), last_username varchar(16), rank_id int, PRIMARY KEY (uuid), FOREIGN KEY (rank_id) REFERENCES ranks(id))");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void createRankTable(){
        try(Connection connection = mySQLDatabase.getHikari().getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ranks(id INT NOT NULL AUTO_INCREMENT, power INT, name varchar(128), prefix varchar(128), suffix varchar(128), PRIMARY KEY (id))");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void createPermissionsTable(){
        try(Connection connection = mySQLDatabase.getHikari().getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS rank_permissions(id INT NOT NULL AUTO_INCREMENT, rank_id INT, permission varchar(128), PRIMARY KEY (id), FOREIGN KEY (rank_id) REFERENCES ranks(id))");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void createSettingsTable(){
        try(Connection connection = mySQLDatabase.getHikari().getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS event_settings(name VARCHAR(128), value VARCHAR(255), group VARCHAR(255), permission VARCHAR(255))");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
