package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.api.objects.EventBooth;
import dev.imabad.mceventsuite.core.api.objects.EventBoothPlot;
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
import redis.clients.jedis.Transaction;

//TODO: Move into booths module
public class BoothDAO extends DAO {

    public BoothDAO(MySQLDatabase mySQLDatabase){
        super(mySQLDatabase);
    }

    public Collection<EventBooth> getBooths(int perPage, int page){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booths INNER JOIN booths_players ON booths.id=booths_players.EventBooth_id LIMIT ? OFFSET ?");
            statement.setInt(1, perPage);
            statement.setInt(2, (perPage * page) - perPage);
            ResultSet resultSet = statement.executeQuery();
            Map<String, EventBooth> boothMap = new HashMap<>();
            while(resultSet.next()){
                String uuid = resultSet.getString("id");
                EventBooth eventBooth = boothMap.get(uuid);
                if(eventBooth == null){
                    String boothType = resultSet.getString("boothType");
                    String name = resultSet.getString("name");
                    String plotID = resultSet.getString("plotID");
                    String status = resultSet.getString("status");
                    String ownerUUID = resultSet.getString("owner_uuid");
                    EventPlayer owner = mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString(ownerUUID));
                    List<EventPlayer> members = new ArrayList<>();
                    eventBooth = new EventBooth(uuid, name, boothType, owner, members, plotID, status);
                    boothMap.put(uuid, eventBooth);
                }
                eventBooth.getMembers().add(mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString("members_uuid")));
            }
            return boothMap.values();
        } catch (SQLException throwables) {
            return Collections.emptyList();
        }
    }

    public List<EventBoothPlot> getPlots(){
        Session session = mySQLDatabase.getSession();
        try {
            return session.createQuery("select r from EventBoothPlot r LEFT JOIN FETCH r.booth b", EventBoothPlot.class).list();
        } finally {
            session.close();
        }
    }

    public EventBooth getBoothFromID(UUID uuid){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booths INNER JOIN booths_players ON booths.id=booths_players.EventBooth_id WHERE id = ?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            EventBooth booth = null;
            while(resultSet.next()){
                if(booth == null){
                    String boothType = resultSet.getString("boothType");
                    String name = resultSet.getString("name");
                    String plotID = resultSet.getString("plotID");
                    String status = resultSet.getString("status");
                    String ownerUUID = resultSet.getString("owner_uuid");
                    EventPlayer owner = mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString(ownerUUID));
                    List<EventPlayer> members = new ArrayList<>();
                    booth = new EventBooth(uuid.toString(), name, boothType, owner, members, plotID, status);
                }
                booth.getMembers().add(mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString("members_uuid")));
            }
            return booth;
        } catch (SQLException throwables) {
            return null;
        }
    }

    public Collection<EventBooth> getPlayerBooths(EventPlayer player){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booths INNER JOIN booths_players ON booths.id=booths_players.EventBooth_id WHERE owner_uuid = ? OR WHERE booths.id IN (SELECT EventBooth_id FROM booths_players WHERE booths_players.members_uuid = ?)");
            statement.setString(1, player.getUUID().toString());
            statement.setString(2, player.getUUID().toString());
            ResultSet resultSet = statement.executeQuery();
            Map<String, EventBooth> boothMap = new HashMap<>();
            while(resultSet.next()){
                String uuid = resultSet.getString("id");
                EventBooth eventBooth = boothMap.get(uuid);
                if(eventBooth == null){
                    String boothType = resultSet.getString("boothType");
                    String name = resultSet.getString("name");
                    String plotID = resultSet.getString("plotID");
                    String status = resultSet.getString("status");
                    String ownerUUID = resultSet.getString("owner_uuid");
                    EventPlayer owner = mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString(ownerUUID));
                    List<EventPlayer> members = new ArrayList<>();
                    eventBooth = new EventBooth(uuid, name, boothType, owner, members, plotID, status);
                    boothMap.put(uuid, eventBooth);
                }
                eventBooth.getMembers().add(mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString("members_uuid")));
            }
            return boothMap.values();
        } catch (SQLException throwables) {
            return Collections.emptyList();
        }
    }

    public EventBooth getBoothFromPlotID(String type, String plotID){
        try (Connection connection = mySQLDatabase.getSession()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM booths INNER JOIN booths_players ON booths.id=booths_players.EventBooth_id WHERE boothType = ? AND plotID = ?");
            statement.setString(1, type);
            statement.setString(2, plotID);
            ResultSet resultSet = statement.executeQuery();
            EventBooth booth = null;
            while(resultSet.next()){
                if(booth == null){
                    String boothType = resultSet.getString("boothType");
                    String uuid = resultSet.getString("uuid");
                    String name = resultSet.getString("name");
                    String status = resultSet.getString("status");
                    String ownerUUID = resultSet.getString("owner_uuid");
                    EventPlayer owner = mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString(ownerUUID));
                    List<EventPlayer> members = new ArrayList<>();
                    booth = new EventBooth(uuid, name, boothType, owner, members, plotID, status);
                }
                booth.getMembers().add(mySQLDatabase.getDAO(PlayerDAO.class).getPlayer(UUID.fromString("members_uuid")));
            }
            return booth;
        } catch (SQLException throwables) {
            return null;
        }
    }

    public void saveBooth(EventBooth booth){
        Transaction tx = null;
        try (Session session = mySQLDatabase.getSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(booth);
            tx.commit();
        } catch (RuntimeException e) {
            assert tx != null;
            tx.rollback();
        }
    }

    public void saveBoothPlot(EventBoothPlot plot){
        Transaction tx = null;
        try (Session session = mySQLDatabase.getSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(plot);
            tx.commit();
        } catch (RuntimeException e) {
            assert tx != null;
            tx.rollback();
        }
    }
}
