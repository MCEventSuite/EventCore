package dev.imabad.mceventsuite.core.config;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.objects.EventSetting;
import dev.imabad.mceventsuite.core.modules.mongo.MongoModule;

public class EventSettings {

    private EventSetting eventName = new EventSetting("eventName", "MCEventSuite Event", "core", "settings.core.name");
    private EventSetting eventIP = new EventSetting("eventIP", "", "core", "settings.core.ip");
    private EventSetting eventOrganiser =  new EventSetting("eventOrganiser", "", "core", "settings.core.organiser");


    public EventSettings(){
        this.eventName = EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().getSetting(eventName);
        this.eventIP = EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().getSetting(eventIP);
        this.eventOrganiser = EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().getSetting(eventOrganiser);
    }

    public String getEventName() {
        return (String) eventName.getValue();
    }

    public String getEventIP() {
        return (String) eventIP.getValue();
    }

    public String getEventOrganiser() {
        return (String) eventOrganiser.getValue();
    }

    public void save(){
        EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().save(eventName);
        EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().save(eventIP);
        EventCore.getInstance().getModuleRegistry().getModule(MongoModule.class).getMongoDatabase().save(eventOrganiser);
    }
}
