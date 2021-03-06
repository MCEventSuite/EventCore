package dev.imabad.mceventsuite.core.api.objects;


import com.google.gson.Gson;
import dev.imabad.mceventsuite.core.util.GsonUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_settings")
public class EventSetting {

    private String name;
    private String value;
    private String group;
    private String permission;

    public EventSetting(String name, Object defaultValue, String group, String permission){
        this.name = name;
        this.value = GsonUtils.getGson().toJson(defaultValue);
        this.group = group;
        this.permission = permission;
    }

    public EventSetting() {
    }

    @Column(name = "group")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Id
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "value")
    private String getValue() {
        return value;
    }

    public <T> T getValueObject(Class<T> clazz) {
        return GsonUtils.getGson().fromJson(value, clazz);
    }

    public void setValue(Object value) {
        this.value =  GsonUtils.getGson().toJson(value);
    }

    @Column(name = "permission")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
