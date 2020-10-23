package dev.imabad.mceventsuite.core.modules.mysql.dao;

import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;

public abstract class DAO {

    protected MySQLDatabase mySQLDatabase;

    public DAO(MySQLDatabase mySQLDatabase){
        this.mySQLDatabase = mySQLDatabase;
        setup();
    }

    public abstract void setup();
}
