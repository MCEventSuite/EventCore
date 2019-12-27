package dev.imabad.mceventsuite.core.api.events;

import dev.imabad.mceventsuite.core.api.events.CoreEvent;
import dev.imabad.mceventsuite.core.api.player.IPlayer;

public class JoinEvent extends CoreEvent {

    private IPlayer iPlayer;

    public JoinEvent(IPlayer player){
        this.iPlayer = player;
    }

    public IPlayer getPlayer() {
        return iPlayer;
    }
}
