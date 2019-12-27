package dev.imabad.mceventsuite.core.api.commands;

import java.util.function.BiConsumer;

public class EventCommandBuilder {

    private EventCommand eventCommand;

    public EventCommandBuilder(String label){
        if(label.length() < 1){
            throw new IllegalArgumentException("label must be valid");
        }
        this.eventCommand = new EventCommand(label);
    }

    public EventCommand build(){
        return this.eventCommand;
    }

    public EventCommandBuilder setPermission(String permission){
        if(permission.length() < 1){
            throw new IllegalArgumentException("permission must be valid");
        }
        this.eventCommand.setPermission(permission);
        return this;
    }

    public EventCommandBuilder setAlias(String... alias){
        if(alias.length < 1){
            throw new IllegalArgumentException("alias must be more than 1");
        }
        this.eventCommand.setAliases(alias);
        return this;
    }

    public EventCommandBuilder addArgument(CommandArgument commandArgument){
        if(commandArgument == null){
            throw new IllegalArgumentException("argument cannot be null");
        }
        this.eventCommand.getArgumentList().add(commandArgument);
        return this;
    }

    public EventCommandBuilder setExecutor(BiConsumer biConsumer){
        if(biConsumer == null){
            throw new IllegalArgumentException("consumer cannot be null");
        }
        this.eventCommand.setExecutor(biConsumer);
        return this;
    }

}
