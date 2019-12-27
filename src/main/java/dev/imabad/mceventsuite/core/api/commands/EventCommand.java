package dev.imabad.mceventsuite.core.api.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EventCommand {

    private String label;
    private String[] aliases;
    private List<CommandArgument> argumentList;
    private String permission;
    private BiConsumer executeCommand;


    protected EventCommand(String label){
        this.label = label;
        this.argumentList = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public List<CommandArgument> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List<CommandArgument> argumentList) {
        this.argumentList = argumentList;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setExecutor(BiConsumer executeCommand){
        this.executeCommand = executeCommand;
    }
}
