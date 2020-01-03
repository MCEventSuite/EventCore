package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;

import java.sql.*;
import java.util.UUID;

public class PlayerDAO {

    private MySQLDatabase mySQLDatabase;

    public PlayerDAO(MySQLDatabase mySQLDatabase){
        this.mySQLDatabase = mySQLDatabase;
    }

    public EventPlayer getPlayer(UUID uuid){
        try(Connection connection = mySQLDatabase.getHikari().getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?;");
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                return new EventPlayer(uuid, result.getString("last_username"), )
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
