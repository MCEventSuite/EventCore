package dev.imabad.mceventsuite.core.modules.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.database.DatabaseProvider;
import dev.imabad.mceventsuite.core.api.database.DatabaseType;
import dev.imabad.mceventsuite.core.api.objects.EventBooth;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;
import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.api.objects.EventSetting;
import dev.imabad.mceventsuite.core.config.database.MySQLConfig;
import dev.imabad.mceventsuite.core.modules.ac.db.AccessControlSetting;
import dev.imabad.mceventsuite.core.modules.ac.db.PlayerBan;
import dev.imabad.mceventsuite.core.modules.audit.db.AuditLogEntry;
import dev.imabad.mceventsuite.core.modules.discord.DiscordLink;
import dev.imabad.mceventsuite.core.modules.mysql.dao.*;
import dev.imabad.mceventsuite.core.modules.mysql.events.MySQLLoadedEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Entity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
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
        String connectionString = mySQLConfig.getPort() != 0
                ? String.format("jdbc:mysql://%s:%d/%s", mySQLConfig.getHostname(), mySQLConfig.getPort(),
                mySQLConfig.getDatabase())
                : String.format("jdbc:mysql://%s/%s", mySQLConfig.getHostname(), mySQLConfig.getDatabase());
        dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        dataSource.setJdbcUrl(connectionString);
        dataSource.setUsername(mySQLConfig.getUsername());
        if(mySQLConfig.getPassword().length() > 0){
            dataSource.setPassword(mySQLConfig.getPassword());
        }
        configuration.addAnnotatedClass(EventSetting.class);
        configuration.addAnnotatedClass(EventPlayer.class);
        configuration.addAnnotatedClass(EventRank.class);
        configuration.addAnnotatedClass(EventBooth.class);
        configuration.addAnnotatedClass(AccessControlSetting.class);
        configuration.addAnnotatedClass(PlayerBan.class);
        configuration.addAnnotatedClass(AuditLogEntry.class);
        configuration.addAnnotatedClass(DiscordLink.class);
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

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
