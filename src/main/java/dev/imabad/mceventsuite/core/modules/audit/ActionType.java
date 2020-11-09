package dev.imabad.mceventsuite.core.modules.audit;

public enum ActionType {

    KICK("Kicked"),
    BAN("Banned"),
    MUTE("Muted");

    String niceName;
    ActionType(String niceName){
        this.niceName = niceName;
    }

    public String getNiceName() {
        return niceName;
    }
}
