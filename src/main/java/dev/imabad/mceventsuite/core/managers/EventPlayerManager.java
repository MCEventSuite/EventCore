package dev.imabad.mceventsuite.core.managers;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.dao.PlayerDAO;

import java.util.HashMap;
import java.util.UUID;

public class EventPlayerManager {

    private HashMap<UUID, EventPlayer> players = new HashMap<>();

    public void addPlayer(EventPlayer player){
        players.put(player.getUUID(), player);
    }
    
    public void removePlayer(EventPlayer player){
        players.remove(player.getUUID());
    }

    public EventPlayer getPlayer(UUID uuid){
        return players.getOrDefault(uuid, null);
    }

}
