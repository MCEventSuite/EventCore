package dev.imabad.mceventsuite.core.modules.audit;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.modules.audit.db.AuditLogDAO;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import dev.imabad.mceventsuite.core.modules.redis.RedisModule;

import java.util.Arrays;
import java.util.List;

public class AuditLogModule extends Module {

    @Override
    public String getName() {
        return "audit";
    }

    @Override
    public void onEnable() {
        MySQLDatabase mySQLDatabase = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase();
        EventCore.getInstance().getEventRegistry().registerListener(MySQLLoadedEvent.class, mySQLLoadedEvent -> {
            mySQLDatabase.registerDAOs(new AuditLogDAO(mySQLDatabase));
        });
    }

    @Override
    public void onDisable() {

    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Arrays.asList(RedisModule.class, MySQLModule.class);
    }

}
