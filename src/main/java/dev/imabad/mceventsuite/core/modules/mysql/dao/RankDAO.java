package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RankDAO extends DAO {

    private Map<Integer, EventRank> ranks;

    public RankDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public Optional<EventRank> getRankByName(String name){
        if(ranks == null){
            getRanks(true);
        }
        return ranks.values().stream().filter(eventRank -> eventRank.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Map<Integer, EventRank> getRanks(boolean forceRefresh){
        if(ranks != null && !forceRefresh){
            return ranks;
        }
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ranks INNER JOIN EventRank_permissions ON ranks.id=EventRank_permissions.EventRank_id");
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, EventRank> rankMap = new HashMap<>();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                EventRank rank = rankMap.get(id);
                if(rank == null){
                    String name = resultSet.getString("name");
                    boolean inheritsFromBelow = resultSet.getBoolean("inheritsFromBelow");
                    int power = resultSet.getInt("power");
                    String prefix = resultSet.getString("prefix");
                    String suffix = resultSet.getString("suffix");
                    String chatColor = resultSet.getString("chatColor");
                    List<String> permissions = new ArrayList<>();
                    rank = new EventRank(id, power, name, prefix, suffix, permissions, inheritsFromBelow, chatColor);
                    rankMap.put(id, rank);
                }
                rank.getPermissions().add(resultSet.getString("permissions"));
            }
            ranks = rankMap;
            return ranks;
        } catch(SQLException sqlException){
            sqlException.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public EventRank getRankByID(int id){
        return getRanks().get(id);
    }

    public Map<Integer, EventRank> getRanks(){
        return getRanks(false);
    }

    public boolean insertRank(EventRank rank){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO ranks (power, name, inheritsFromBelow, prefix, suffix, chatColor) VALUES (?, ?, ?, ?, ?, ?);");
            insertStatement.setInt(1, rank.getPower());
            insertStatement.setString(2, rank.getName());
            insertStatement.setBoolean(3, rank.isInheritsFromBelow());
            insertStatement.setString(4, rank.getPrefix());
            insertStatement.setString(5, rank.getSuffix());
            insertStatement.setString(6, rank.getChatColor());
            if(insertStatement.executeUpdate() == 0){
                return false;
            }
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newID = generatedKeys.getInt(1);
                    rank.setId(newID);
                    updateRankPermissions(rank);
                    return true;
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
        return false;
    }

    public void updateRank(EventRank rank){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement insertStatement = connection.prepareStatement("UPDATE ranks SET power = ?, name = ?, inheritsFromBelow = ?, prefix = ? suffix = ?, chatColor = ? WHERE id = ?");
            insertStatement.setInt(1, rank.getPower());
            insertStatement.setString(2, rank.getName());
            insertStatement.setBoolean(3, rank.isInheritsFromBelow());
            insertStatement.setString(4, rank.getPrefix());
            insertStatement.setString(5, rank.getSuffix());
            insertStatement.setString(6, rank.getChatColor());
            insertStatement.setInt(7, rank.getId());
            insertStatement.executeUpdate();
            updateRankPermissions(rank);
        }catch(SQLException exception){
            //TODO: Add proper logging
            exception.printStackTrace();
        }
    }

    public Collection<String> getRankPermissions(EventRank rank){
        try(Connection connection = mySQLDatabase.getSession()){
            PreparedStatement selectStatement = connection.prepareStatement("SELECT permissions FROM EventRank_permissions WHERE EventRank_id = ?");
            selectStatement.setInt(1, rank.getId());
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

    public void updateRankPermissions(EventRank rank){
        try(Connection connection = mySQLDatabase.getSession()){
            Collection<String> toAdd = rank.getPermissions();
            if(rank.getId() != 0){
                Collection<String> existingPermissions = getRankPermissions(rank);
                toAdd = toAdd.stream().filter(s -> !existingPermissions.contains(s)).collect(
                    Collectors.toList());
                Collection<String> toRemove = existingPermissions.stream().filter(s -> !rank.getPermissions().contains(s)).collect(
                    Collectors.toList());
                if(!toRemove.isEmpty()){
                    PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM EventRank_permissions WHERE EventRank_id = ? AND permissions IN (?)");
                    removeStatement.setInt(1, rank.getId());
                    removeStatement.setArray(2, connection.createArrayOf("VARCHAR", toRemove.toArray()));
                    removeStatement.executeUpdate();
                }
            }
            if(!toAdd.isEmpty()){
                PreparedStatement addStatement = connection.prepareStatement("INSERT INTO EventRank_permissions VALUES(?,?)");
                addStatement.setInt(1, rank.getId());
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

    public Optional<EventRank> getLowestRank(){
        if(ranks == null){
            getRanks(true);
        }
        if(ranks.size() < 1){
            return Optional.of(new EventRank(0, "Default", "", "", Collections.emptyList()));
        }
        return ranks.values().stream().min(Comparator.comparingInt(EventRank::getPower));
    }
}
