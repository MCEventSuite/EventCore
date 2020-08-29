package dev.imabad.mceventsuite.core.api.actions;

import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

public interface IActionExecutor {

    void execute(Action action, EventPlayer player);

}
