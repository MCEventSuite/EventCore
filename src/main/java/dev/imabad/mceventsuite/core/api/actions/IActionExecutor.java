package dev.imabad.mceventsuite.core.api.actions;

import dev.imabad.mceventsuite.core.api.player.IPlayer;

public interface IActionExecutor {

    void execute(Action action, IPlayer player);

}
