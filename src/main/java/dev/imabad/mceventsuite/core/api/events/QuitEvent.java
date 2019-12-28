package dev.imabad.mceventsuite.core.api.events;

import dev.imabad.mceventsuite.core.api.player.IPlayer;

public class QuitEvent extends CoreEvent {

    private IPlayer iPlayer;

    public QuitEvent(IPlayer player){
        this.iPlayer = player;
    }

    public IPlayer getPlayer() {
        return iPlayer;
    }
}
