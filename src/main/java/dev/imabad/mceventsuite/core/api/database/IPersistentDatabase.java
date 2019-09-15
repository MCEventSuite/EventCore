package dev.imabad.mceventsuite.core.api.database;

import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.api.modules.ModuleConfig;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;

import java.util.List;
import java.util.UUID;

public interface IPersistentDatabase extends IDatabase {

    EventPlayer getOrCreatePlayer(UUID uuid, String username);

    EventPlayer getPlayer(String username);

    EventPlayer getPlayer(UUID uuid);

    void savePlayer(EventPlayer player);

    List<EventPlayer> getPlayers();

    EventRank getLowestRank();

    List<EventRank> getRanks();

    void saveRank(EventRank eventRank);

    <T> T get(Class<T> object);

    void save(Object object);

    ModuleConfig getModuleConfig(Module module);

}
