package dev.imabad.mceventsuite.core.api.objects;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("settings")
public class EventSetting {

    @Id
    private String name;
    private Object value;
    private String group;
    private String permission;

    public EventSetting(String name, Object defaultValue, String group, String permission){
        this.name = name;
        this.value = defaultValue;
        this.group = group;
        this.permission = permission;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
