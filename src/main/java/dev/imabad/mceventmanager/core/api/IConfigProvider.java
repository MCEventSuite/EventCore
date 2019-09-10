package dev.imabad.mceventmanager.core.api;

public interface IConfigProvider<T extends BaseConfig> {

    Class<T> getConfigType();

    T getConfig();

    String getFileName();

    void loadConfig(T config);

    void saveConfig();
}
