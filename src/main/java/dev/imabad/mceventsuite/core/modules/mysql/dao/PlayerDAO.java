package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.util.GsonUtils;
import dev.imabad.mceventsuite.core.util.PropertyMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerDAO extends DAO {

    public PlayerDAO(MySQLDatabase mySQLDatabase) {
        super(mySQLDatabase);
    }

    public Collection<EventPlayer> getPlayers(){
        return getPlayers(20, 1);
    }

    /**
     * Get X amount of players offset by page
     * @return  A list of players
     * @see     EventPlayer
     */
    public Collection<EventPlayer> getPlayers(int perPage, int page){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players INNER JOIN EventPlayer_permissions ON players.uuid=EventPlayer_permissions.EventPlayer_uuid LIMIT ? OFFSET ?");
            statement.setInt(1, perPage);
            statement.setInt(2, (perPage * page) - perPage);
            ResultSet resultSet = statement.executeQuery();
            Map<String, EventPlayer> playerMap = new HashMap<>();
            while(resultSet.next()){
                String uuid = resultSet.getString("uuid");
                EventPlayer eventPlayer = playerMap.get(uuid);
                if(eventPlayer == null){
                    String lastUsername = resultSet.getString("last_username");
                    String properties = resultSet.getString("properties");
                    int rankID = resultSet.getInt("rank_id");
                    EventRank rank = mySQLDatabase.getDAO(RankDAO.class).getRankByID(rankID);
                    List<String> permissions = new ArrayList<>();
                    PropertyMap propertyMap = GsonUtils.getGson().fromJson(properties, PropertyMap.class);
                    eventPlayer = new EventPlayer(UUID.fromString(uuid), lastUsername, rank, permissions, propertyMap);
                    playerMap.put(uuid, eventPlayer);
                }
                eventPlayer.getPermissions().add(resultSet.getString("permissions"));
            }
            return playerMap.values();
        } catch (SQLException throwables) {
            return Collections.emptyList();
        }
    }

    /**
     * Get or create a player using a UUID or username
     * If a player does not exist a new one is created using the provided UUID and username.
     *
     * @param uuid      - UUID of the player to find or create
     * @param username  - Username of the player to find or create
     * @return          - An instance of EventPlayer
     * @see             EventPlayer
     */
    public EventPlayer getOrCreatePlayer(UUID uuid, String username){
        EventPlayer eventPlayer = getPlayer(uuid);
        if (eventPlayer == null) {
            eventPlayer = getPlayer(username);
            if (eventPlayer == null) {
                eventPlayer = new EventPlayer(uuid, username);
                saveNewPlayer(eventPlayer);
            } else {
                if(!eventPlayer.getUUID().equals(uuid)){
                    eventPlayer.setUUID(uuid);
                }
            }
        }
        return eventPlayer;
    }

    /**
     * Get an EventPlayer by their UUID
     * This is the preferred method
     *
     * @param uuid - UUID of the player to find
     * @return     - An instance of EventPlayer or null
     * @see        EventPlayer
     */
    public EventPlayer getPlayer(UUID uuid){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players INNER JOIN EventPlayer_permissions ON players.uuid=EventPlayer_permissions.EventPlayer_uuid WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            EventPlayer player = null;
            while(resultSet.next()){
                if(player == null){
                    String lastUsername = resultSet.getString("last_username");
                    String properties = resultSet.getString("properties");
                    int rankID = resultSet.getInt("rank_id");
                    EventRank rank = mySQLDatabase.getDAO(RankDAO.class).getRankByID(rankID);
                    List<String> permissions = new ArrayList<>();
                    PropertyMap propertyMap = GsonUtils.getGson().fromJson(properties, PropertyMap.class);
                    player = new EventPlayer(uuid, lastUsername, rank, permissions, propertyMap);
                }
                player.getPermissions().add(resultSet.getString("permissions"));
            }
            return player;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get an EventPlayer by their username
     * Avoid this where possible.
     *
     * @param username  - Username of the player to find
     * @return          - the player with that username or null
     * @see             EventPlayer
     */
    public EventPlayer getPlayer(String username){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players INNER JOIN EventPlayer_permissions ON players.uuid=EventPlayer_permissions.EventPlayer_uuid WHERE last_username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            EventPlayer player = null;
            while(resultSet.next()){
                if(player == null){
                    String uuid = resultSet.getString("uuid");
                    String properties = resultSet.getString("properties");
                    int rankID = resultSet.getInt("rank_id");
                    EventRank rank = mySQLDatabase.getDAO(RankDAO.class).getRankByID(rankID);
                    List<String> permissions = new ArrayList<>();
                    PropertyMap propertyMap = GsonUtils.getGson().fromJson(properties, PropertyMap.class);
                    player = new EventPlayer(UUID.fromString(uuid), username, rank, permissions, propertyMap);
                }
                player.getPermissions().add(resultSet.getString("permissions"));
            }
            return player;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveNewPlayer(EventPlayer player){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO players VALUES (?, ?, ?, ?);");
            insertStatement.setString(1, player.getUUID().toString());
            insertStatement.setString(2, player.getLastUsername());
            insertStatement.setString(3, GsonUtils.getGson().toJson(player.getProperties()));
            insertStatement.setInt(4, player.getRank().getId());
            if(insertStatement.executeUpdate() == 0){
                return false;
            }
            updatePlayerPermissions(player, false);
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
        return false;
    }

    public void savePlayer(EventPlayer eventPlayer){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement insertStatement = connection.prepareStatement("UPDATE players SET last_username = ?, properties = ?, rank_id = ? WHERE uuid = ?");
            insertStatement.setString(1, eventPlayer.getLastUsername());
            insertStatement.setString(2, GsonUtils.getGson().toJson(eventPlayer.getProperties()));
            insertStatement.setInt(4, eventPlayer.getRank().getId());
            insertStatement.setString(4, eventPlayer.getUUID().toString());
            insertStatement.executeUpdate();
            updatePlayerPermissions(eventPlayer, true);
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
    }

    public Collection<String> getPlayerPermissions(EventPlayer eventPlayer){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement selectStatement = connection.prepareStatement("SELECT permissions FROM EventPlayer_permissions WHERE EventPlayer_uuid = ?");
            selectStatement.setString(1, eventPlayer.getUUID().toString());
            ResultSet resultSet = selectStatement.executeQuery();
            Collection<String> permissions = new ArrayList<>();
            while(resultSet.next()){
                permissions.add(resultSet.getString("permissions"));
            }
            return permissions;
        }catch (SQLException e){
            return Collections.emptyList();
        }
    }

    public void updatePlayerPermissions(EventPlayer eventPlayer, boolean isUpdate){
        try(Connection connection = mySQLDatabase.getSession()){
            Collection<String> toAdd = eventPlayer.getPermissions();
            if(isUpdate){
                Collection<String> existingPermissions = getPlayerPermissions(eventPlayer);
                toAdd = toAdd.stream().filter(s -> !existingPermissions.contains(s)).collect(
                    Collectors.toList());
                Collection<String> toRemove = existingPermissions.stream().filter(s -> !eventPlayer.getPermissions().contains(s)).collect(
                    Collectors.toList());
                if(!toRemove.isEmpty()){
                    PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM EventPlayer_permissions WHERE EventPlayer_uuid = ? AND permissions IN (?)");
                    removeStatement.setString(1, eventPlayer.getUUID().toString());
                    removeStatement.setArray(2, connection.createArrayOf("VARCHAR", toRemove.toArray()));
                    removeStatement.executeUpdate();
                }
            }
            if(!toAdd.isEmpty()){
                PreparedStatement addStatement = connection.prepareStatement("INSERT INTO EventPlayer_permissions VALUES(?,?)");
                addStatement.setString(1, eventPlayer.getUUID().toString());
                toAdd.forEach(s -> {
                    try {
                        addStatement.setString(2, s);
                        addStatement.addBatch();
                    } catch(SQLException exception){
                        exception.printStackTrace();
                    }
                });
                addStatement.executeBatch();
            }
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
    }
}
