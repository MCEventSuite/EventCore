package dev.imabad.mceventsuite.core.api.events;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

public class QuitEvent extends CoreEvent {

    private EventPlayer iPlayer;

    public QuitEvent(EventPlayer player){
        this.iPlayer = player;
    }

    public EventPlayer getPlayer() {
        return iPlayer;
    }
}
