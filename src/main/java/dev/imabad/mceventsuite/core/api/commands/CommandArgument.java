package dev.imabad.mceventsuite.core.api.commands;

public class CommandArgument {

    private String name;
    private ArgumentType argumentType;
    private String description;

    public CommandArgument(String name, ArgumentType argumentType, String description) {
        this.name = name;
        this.argumentType = argumentType;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public ArgumentType getArgumentType() {
        return argumentType;
    }

    public String getDescription() {
        return description;
    }
}
