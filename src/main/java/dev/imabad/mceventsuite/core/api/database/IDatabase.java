package dev.imabad.mceventsuite.core.api.database;

public interface IDatabase {

    boolean isConnected();

    DatabaseType getType();

    String getName();

    void connect();

    void disconnect();
}
