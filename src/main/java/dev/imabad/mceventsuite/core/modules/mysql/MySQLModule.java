package dev.imabad.mceventsuite.core.modules.mysql;

import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.config.database.MySQLConfig;

import java.util.Collections;
import java.util.List;

public class MySQLModule extends Module implements IConfigProvider<MySQLConfig> {

    private MySQLConfig mySQLConfig;
    private MySQLDatabase mySQLDatabase;

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public void onEnable() {
        this.mySQLDatabase = new MySQLDatabase(this.mySQLConfig);
        this.mySQLDatabase.connect();
    }

    @Override
    public void onDisable() {
        if(this.mySQLDatabase.isConnected())
            this.mySQLDatabase.disconnect();
    }

    @Override
    public List<Module> getDependencies() {
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


    public MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }
}
