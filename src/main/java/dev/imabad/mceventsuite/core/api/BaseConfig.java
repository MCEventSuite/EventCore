package dev.imabad.mceventsuite.core.api;

public abstract class BaseConfig {

    public abstract String getName();

    public String getFileName() {
        return getName().concat(".json");
    }

}
