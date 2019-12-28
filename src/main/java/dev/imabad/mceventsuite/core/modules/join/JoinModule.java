package dev.imabad.mceventsuite.core.modules.join;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.events.JoinEvent;
import dev.imabad.mceventsuite.core.api.modules.Module;

import java.util.Arrays;
import java.util.List;

public class JoinModule extends Module {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public void onEnable() {
        EventCore.getInstance().getEventRegistry().registerListener(JoinEvent.class, this::handleJoinEvent);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public List<Module> getDependencies() {
        return Arrays.asList();
    }

    public void handleJoinEvent(JoinEvent joinEvent){
        joinEvent.getPlayer().sendMessage("Welcome!");
    }

}
