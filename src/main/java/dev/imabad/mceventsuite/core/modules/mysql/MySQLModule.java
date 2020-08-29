package dev.imabad.mceventsuite.core.modules.mysql;

import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.config.database.MySQLConfig;

import java.util.Collections;
import java.util.List;

public class MySQLModule extends Module implements IConfigProvider<MySQLConfig> {

    private MySQLConfig mySQLConfig;
    private MySQLDatabase mySQLDatabase;
    private Thread databaseThread;

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public void onEnable() {
        if (this.mySQLConfig.getHostname().isEmpty()) {
            throw new IllegalArgumentException(
                    "The required property 'hostname' is missing from the connection config.\n");
        }

        if (this.mySQLConfig.getDatabase().isEmpty()) {
            throw new IllegalArgumentException(
                    "The required property 'database' is missing from the connection config.\n");
        }

        if (this.mySQLConfig.getUsername().isEmpty()) {
            throw new IllegalArgumentException(
                    "The required property 'username' is missing from the connection config.\n");
        }

        this.mySQLDatabase = new MySQLDatabase(this.mySQLConfig);
        databaseThread = new Thread(mySQLDatabase::connect);
        databaseThread.setContextClassLoader(getClass().getClassLoader());
        databaseThread.start();
    }

    @Override
    public void onDisable() {
        if(this.mySQLDatabase.isConnected())
            this.mySQLDatabase.disconnect();
        if(databaseThread != null && databaseThread.isAlive())
            databaseThread.stop();
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Class<MySQLConfig> getConfigType() {
        return MySQLConfig.class;
    }

    @Override
    public MySQLConfig getConfig() {
        return mySQLConfig;
    }


    @Override
    public String getFileName() {
        return "mysql.json";
    }

    @Override
    public void loadConfig(MySQLConfig config) {
        this.mySQLConfig = config;
    }

    @Override
    public void saveConfig() {

    }

    @Override
    public boolean saveOnQuit() {
        return false;
    }


    public MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }
}
