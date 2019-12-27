package dev.imabad.mceventsuite.core;

import dev.imabad.mceventsuite.core.api.commands.ArgumentType;
import dev.imabad.mceventsuite.core.api.commands.CommandArgument;
import dev.imabad.mceventsuite.core.api.commands.EventCommand;
import dev.imabad.mceventsuite.core.api.commands.EventCommandBuilder;
import dev.imabad.mceventsuite.core.api.events.CoreEvent;
import dev.imabad.mceventsuite.core.registries.EventRegistry;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventCommandBuilderTest {


    @Test
    public void commandLabelCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            EventCommandBuilder builder = new EventCommandBuilder("");
        });
    }

    @Test
    public void permissionCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            EventCommandBuilder builder = new EventCommandBuilder("command");
            builder.setPermission("");
        });
    }

    @Test
    public void aliasCannotBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            EventCommandBuilder builder = new EventCommandBuilder("command");
            builder.setAlias();
        });
    }

    @Test
    public void argumentCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            EventCommandBuilder builder = new EventCommandBuilder("command");
            builder.addArgument(null);
        });
    }

    @Test
    public void executorCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            EventCommandBuilder builder = new EventCommandBuilder("command");
            builder.setExecutor(null);
        });
    }

    @Test
    public void builderWorks(){
        EventCommandBuilder builder = new EventCommandBuilder("command");
        CommandArgument argumentName = new CommandArgument("name", ArgumentType.STRING, "Name of the command");
        builder.setAlias("c").setPermission("command.permission").setExecutor((a, b) -> {}).addArgument(argumentName);
        EventCommand eventCommand = builder.build();
        assertEquals(eventCommand.getAliases().length, 1);
        assertEquals(eventCommand.getAliases()[0], "c");
        assertEquals(eventCommand.getPermission(), "command.permission");
        assertEquals(eventCommand.getArgumentList().size(), 1);
        assertEquals(eventCommand.getArgumentList().get(0), argumentName);
        assertEquals(eventCommand.getArgumentList().get(0).getName(), "name");
    }
}
