package dev.imabad.mceventsuite.core.managers;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.player.IPlayer;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;

import java.util.HashMap;
import java.util.UUID;

public class EventPlayerManager {

    private HashMap<UUID, IPlayer> players = new HashMap<>();

    public EventPlayer addPlayer(IPlayer player){
        players.put(player.getUUID(), player);
        return EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getPlayer(player.getUUID());
    }

    public IPlayer getPlayerFromRef(EventPlayer player){
        return players.get(player.getUuid());
    }

    public void removePlayer(IPlayer player){
        players.remove(player.getUUID());
    }

    public IPlayer getPlayer(UUID uuid){
        return players.getOrDefault(uuid, null);
    }

}
