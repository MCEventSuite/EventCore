package dev.imabad.mceventsuite.core.config;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.objects.EventSetting;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLDatabase;
import dev.imabad.mceventsuite.core.modules.mysql.MySQLModule;
import dev.imabad.mceventsuite.core.modules.mysql.dao.SettingDAO;

public class EventSettings {

    private EventSetting eventName = new EventSetting("eventName", "MCEventSuite Event", "core", "settings.core.name");
    private EventSetting eventIP = new EventSetting("eventIP", "", "core", "settings.core.ip");
    private EventSetting eventOrganiser =  new EventSetting("eventOrganiser", "", "core", "settings.core.organiser");


    public EventSettings(){
        this.eventName = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(SettingDAO.class).getSetting(eventName);
        this.eventIP = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(SettingDAO.class).getSetting(eventIP);
        this.eventOrganiser = EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(SettingDAO.class).getSetting(eventOrganiser);
    }

    public String getEventName() {
        return eventName.getValueObject(String.class);
    }

    public String getEventIP() {
        return eventIP.getValueObject(String.class);
    }

    public String getEventOrganiser() {
        return eventOrganiser.getValueObject(String.class);
    }

    public void save(){
        EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(SettingDAO.class).saveSetting(eventName);
        EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(SettingDAO.class).saveSetting(eventIP);
        EventCore.getInstance().getModuleRegistry().getModule(MySQLModule.class).getMySQLDatabase().getDAO(SettingDAO.class).saveSetting(eventOrganiser);
    }
}
