package dev.imabad.mceventsuite.core.api;

import dev.imabad.mceventsuite.core.api.actions.Action;

public interface PlayerActions {

   void sendMessage(String message);

   void executeAction(Action action);
}
