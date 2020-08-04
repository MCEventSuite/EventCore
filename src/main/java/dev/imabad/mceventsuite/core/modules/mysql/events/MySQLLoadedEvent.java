package dev.imabad.mceventsuite.core.modules.mysql.events;

import dev.imabad.mceventsuite.core.api.events.CoreEvent;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;

public class MySQLLoadedEvent extends CoreEvent {

    private MySQLDatabase mySQLDatabase;

    public MySQLLoadedEvent(MySQLDatabase mySQLDatabase){
        this.mySQLDatabase = mySQLDatabase;
    }

    public MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }
}
