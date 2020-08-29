package dev.imabad.mceventsuite.core.api.objects;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ranks")
public class EventRank {

    private int id;
    private int power;
    private String name;
    private String prefix;
    private String suffix;
    private List<String> permissions;
    private boolean inheritsFromBelow = true;


    public EventRank(int power, String name, String prefix, String suffix, List<String> permissions, boolean inheritsFromBelow) {
        this.power = power;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
        this.inheritsFromBelow = inheritsFromBelow;
    }

    public EventRank(int power, String name, String prefix, String suffix, List<String> permissions) {
        this(power, name, prefix, suffix, permissions, true);
    }

    public EventRank(int id, int power, String name, String prefix, String suffix, List<String> permissions, boolean inheritsFromBelow){
        this(power, name, prefix, suffix, permissions, inheritsFromBelow);
        this.id = id;
    }

    public EventRank() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    public int getId() {
        return id;
    }

    @Column(name = "power")
    public int getPower() {
        return power;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "prefix")
    public String getPrefix() {
        return prefix;
    }

    @Column(name = "suffix")
    public String getSuffix() {
        return suffix;
    }

    @ElementCollection
    public List<String> getPermissions() {
        return permissions;
    }

    @Column(name = "inheritsFromBelow")
    public boolean isInheritsFromBelow() {
        return inheritsFromBelow;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void setInheritsFromBelow(boolean inheritsFromBelow) {
        this.inheritsFromBelow = inheritsFromBelow;
    }

    public boolean hasPermission(String permission){
        return this.permissions.contains(permission) || (!this.permissions.contains('-' + permission) && this.permissions.contains('+' + permission));
    }
}
