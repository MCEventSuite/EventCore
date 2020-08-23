package dev.imabad.mceventsuite.core.api.events;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

public class JoinEvent extends CoreEvent {

    private EventPlayer iPlayer;

    public JoinEvent(EventPlayer player){
        this.iPlayer = player;
    }

    public EventPlayer getPlayer() {
        return iPlayer;
    }
}
