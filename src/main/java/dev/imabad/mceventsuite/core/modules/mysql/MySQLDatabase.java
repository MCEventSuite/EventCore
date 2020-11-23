package dev.imabad.mceventsuite.core.modules.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.database.DatabaseProvider;
import dev.imabad.mceventsuite.core.api.database.DatabaseType;
import dev.imabad.mceventsuite.core.config.database.MySQLConfig;
import dev.imabad.mceventsuite.core.modules.mysql.dao.BoothDAO;
import dev.imabad.mceventsuite.core.modules.mysql.dao.DAO;
import dev.imabad.mceventsuite.core.modules.mysql.dao.InvalidDAOException;
import dev.imabad.mceventsuite.core.modules.mysql.dao.PlayerDAO;
import dev.imabad.mceventsuite.core.modules.mysql.dao.RankDAO;
import dev.imabad.mceventsuite.core.modules.mysql.dao.SettingDAO;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MySQLDatabase extends DatabaseProvider {

    private MySQLConfig mySQLConfig;
    private HikariDataSource dataSource;

    private Set<DAO> registeredDAOs;

    public MySQLDatabase(MySQLConfig mySQLConfig){
        this.mySQLConfig = mySQLConfig;
        this.registeredDAOs = new HashSet<>();
        registerDAOs(
                new PlayerDAO(this),
                new RankDAO(this),
                new SettingDAO(this),
                new BoothDAO(this)
        );
    }

    @Override
    public boolean isConnected() {
        return !dataSource.isClosed();
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
        HikariConfig config = new HikariConfig();

        String connectionString = mySQLConfig.getPort() != 0
            ? String.format("jdbc:mysql://%s:%d/%s", mySQLConfig.getHostname(), mySQLConfig.getPort(),
                mySQLConfig.getDatabase())
            : String.format("jdbc:mysql://%s/%s", mySQLConfig.getHostname(), mySQLConfig.getDatabase());

        config.setJdbcUrl(connectionString);
        config.setUsername(mySQLConfig.getUsername());

        if(mySQLConfig.getPassword().length() > 0) {
            config.setPassword(mySQLConfig.getPassword());
        }
        dataSource = new HikariDataSource(config);
        EventCore.getInstance().getEventRegistry().handleEvent(new MySQLLoadedEvent(this));
    }

    public void registerDAOs(DAO... daos){
        registeredDAOs.addAll(Arrays.asList(daos));
    }

    public <T extends DAO> T getDAO(Class<T> dao){
        return registeredDAOs.stream().filter(dao::isInstance).map(dao::cast).findFirst().orElseThrow(() -> new InvalidDAOException(dao));
    }

    @Override
    public void disconnect() {
        if(dataSource != null)
            dataSource.close();
    }

    public Connection getSession() throws SQLException {
        return dataSource.getConnection();
    }
}
