package dev.imabad.mceventmanager.core.api;

public abstract class BaseConfig {

    public abstract String getName();

    public String getFileName() {
        return getName().concat(".json");
    }

}
