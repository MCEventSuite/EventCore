package dev.imabad.mceventmanager.core.api;

public abstract class DatabaseProvider<T extends BaseConfig> implements IConfigProvider<T> {

    private boolean connected = false;

    public abstract String getName();

    public void connect() {
        connected = true;
    }

    public void disconnect() {
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }
}
