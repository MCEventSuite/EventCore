package dev.imabad.mceventsuite.core.api.objects;


import dev.imabad.mceventsuite.core.util.SpecialTag;

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
    private String chatColor = "&fff";
    private List<String> permissions;
    private boolean inheritsFromBelow = true;
    private int initialEventPassXP = 0;


    public EventRank(int power, String name, String prefix, String suffix, List<String> permissions, boolean inheritsFromBelow) {
        this.power = power;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.chatColor = "&fff";
        this.permissions = permissions;
        this.inheritsFromBelow = inheritsFromBelow;
        this.initialEventPassXP = 0;
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
        return this.suffix;
    }

    @Transient
    private String getFormattedPrefix(boolean bedrock) {
        String modifiedPrefix = this.getPrefix();

        if (prefix.contains("#")) {
            int pos = prefix.indexOf("#");
            int end = prefix.indexOf(";");
            String code = prefix.substring(pos + 1, end);
            SpecialTag tag = SpecialTag.valueOf(code);

            if(tag.getBedrockString().contains("[") && bedrock) {
                modifiedPrefix = prefix.substring(0, pos) + tag.getBedrockString()
                        + prefix.substring(end + 1) + " ";
            } else {
                modifiedPrefix = "&f" +
                        (bedrock ? tag.getBedrockString() : tag.getJavaString())
                        + prefix.substring(0, pos) + prefix.substring(end + 1) + " ";
            }
        }

        return modifiedPrefix;
    }

    @Transient
    public String getJavaPrefix() {
        return this.getFormattedPrefix(false);
    }

    @Transient
    public String getBedrockPrefix() {
        return this.getFormattedPrefix(true);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> getPermissions() {
        return permissions;
    }

    @Column(name = "inheritsFromBelow")
    public boolean isInheritsFromBelow() {
        return inheritsFromBelow;
    }

    @Column(name = "chatColor")
    public String getChatColor() {
        if(chatColor == null){
            return "#fff";
        }
        return chatColor;
    }

    @Column(name = "initialXP")
    public int getInitialEventPassXP() {
        return initialEventPassXP;
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

    public void setChatColor(String chatColor) {
        this.chatColor = chatColor;
    }

    public void setInitialEventPassXP(int initialEventPassXP) {
        this.initialEventPassXP = initialEventPassXP;
    }

    private boolean containsPermission(String permission){
        return this.permissions.contains(permission) || (!this.permissions.contains('-' + permission) && this.permissions.contains('+' + permission));
    }

    public boolean hasPermission(String permission){
        if(containsPermission("*")){
            return true;
        }
        String[] parts = permission.split("\\.");
        StringBuilder fullPerm = new StringBuilder();
        for(int i = 0; i < parts.length - 1; i++){
            String part = parts[i];
            if(i > 0){
                fullPerm.append(".");
            }
            fullPerm.append(part);
            if(containsPermission(fullPerm + ".*")){
              return true;
            }
        }
        return containsPermission(permission);
    }

    public String nameEnum(){
        return this.name.replace(" ",  "_").toUpperCase();
    }
}
