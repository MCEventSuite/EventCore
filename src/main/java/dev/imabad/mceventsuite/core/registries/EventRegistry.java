package dev.imabad.mceventsuite.core.registries;

import dev.imabad.mceventsuite.core.EventCore;
import dev.imabad.mceventsuite.core.api.IRegistry;
import dev.imabad.mceventsuite.core.api.events.CoreEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventRegistry implements IRegistry {

    private HashMap<Class<? extends CoreEvent>, ArrayList<Consumer>> events;

    public EventRegistry() {
        this.events = new HashMap<>();
    }

    public <T extends CoreEvent> void registerListener(Class<T> event, Consumer<T> runnable){
        if(events.containsKey(event)){
            events.get(event).add(runnable);
        } else {
            ArrayList list = new ArrayList<>();
            list.add(runnable);
            events.put(event, list);
        }
    }

    public <T extends CoreEvent> void handleEvent(T coreEvent){
        if(events.containsKey(coreEvent.getClass())) {
            events.get(coreEvent.getClass()).forEach(runnable -> runnable.accept(coreEvent));
        }
    }

    @Override
    public String getName() {
        return "events";
    }

    @Override
    public boolean isLoaded() {
        return true;
    }
}
