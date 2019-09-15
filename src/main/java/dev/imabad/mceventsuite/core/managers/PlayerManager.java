package dev.imabad.mceventsuite.core.managers;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.player.IPlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private HashMap<UUID, IPlayer> players = new HashMap<>();

    public EventPlayer addPlayer(IPlayer player){
        players.put(player.getUUID(), player);
        return EventCore.getInstance().getDatabaseRegistry().getPersistentDatabase().getPlayer(player.getUUID());
    }

    public IPlayer getPlayerFromRef(EventPlayer player){
        return players.get(player.getUuid());
    }

}
