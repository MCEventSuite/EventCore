package dev.imabad.mceventsuite.core.managers;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

import java.util.*;

public class EventPlayerManager {

    private HashMap<UUID, EventPlayer> players = new HashMap<>();

    public void addPlayer(EventPlayer player){
        players.put(player.getUUID(), player);
    }

    public void removePlayer(EventPlayer player){
        players.remove(player.getUUID());
    }

    public Optional<EventPlayer> getPlayer(UUID uuid){
        return Optional.ofNullable(players.get(uuid));
    }

    public Collection<EventPlayer> getPlayers(){
        return players.values();
    }

    public boolean hasPlayer(UUID uuid) {return players.containsKey(uuid);}

}
