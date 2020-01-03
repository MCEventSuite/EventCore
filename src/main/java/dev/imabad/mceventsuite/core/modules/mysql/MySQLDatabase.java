package dev.imabad.mceventsuite.core.modules.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.imabad.mceventsuite.core.api.database.DatabaseProvider;
import dev.imabad.mceventsuite.core.api.database.DatabaseType;
import dev.imabad.mceventsuite.core.config.database.MySQLConfig;

public class MySQLDatabase extends DatabaseProvider {

    private MySQLConfig mySQLConfig;
    private HikariDataSource hikariDataSource;

    public MySQLDatabase(MySQLConfig mySQLConfig){
        this.mySQLConfig = mySQLConfig;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.PERSISTENT;
    }

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public void connect() {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        hikariDataSource.addDataSourceProperty("serverName", mySQLConfig.getHostname());
        hikariDataSource.addDataSourceProperty("port", mySQLConfig.getPort());
        hikariDataSource.addDataSourceProperty("databaseName", mySQLConfig.getDatabase());
        if(mySQLConfig.getUsername().length() > 0){
            hikariDataSource.addDataSourceProperty("user", mySQLConfig.getUsername());
        }
        if(mySQLConfig.getPassword().length() > 0) {
            hikariDataSource.addDataSourceProperty("password", mySQLConfig.getPassword());
        }
        MySQLTables.createTables();
    }

    @Override
    public void disconnect() {
        if(hikariDataSource != null)
            hikariDataSource.close();
    }

    public HikariDataSource getHikari() {
        return hikariDataSource;
    }
}
