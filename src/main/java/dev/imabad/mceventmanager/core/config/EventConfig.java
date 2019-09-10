package dev.imabad.mceventmanager.core.config;

import dev.imabad.mceventmanager.core.api.BaseConfig;

public class EventConfig extends BaseConfig {

    private String eventName;
    private String eventIP;
    private String eventOrganiser;

    @Override
    public String getName() {
        return "event";
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventIP() {
        return eventIP;
    }

    public String getEventOrganiser() {
        return eventOrganiser;
    }
}
