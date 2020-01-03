package dev.imabad.mceventsuite.core.api.objects;


import java.util.List;

public class EventRank {

    private int id;
    private int power;
    private String name;
    private String prefix;
    private String suffix;
    private List<String> permissions;

    EventRank() {
    }

    public EventRank(int power, String name, String prefix, String suffix, List<String> permissions) {
        this.power = power;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
