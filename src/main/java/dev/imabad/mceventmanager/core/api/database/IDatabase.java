package dev.imabad.mceventmanager.core.api.database;

public interface IDatabase {

    boolean isConnected();

    DatabaseType getType();

    String getName();

    void connect();

    void disconnect();
}
