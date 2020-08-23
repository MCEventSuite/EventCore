package dev.imabad.mceventsuite.core.api.actions;

import dev.imabad.mceventsuite.core.actions.GiveItemAction;
import dev.imabad.mceventsuite.core.api.objects.EventPlayer;

import java.util.Arrays;
import java.util.List;

public enum ActionType {

    GIVE_ITEM(GiveItemAction.AMOUNT, GiveItemAction.LORE, GiveItemAction.MATERIAL, GiveItemAction.NAME, GiveItemAction.SLOT),
    TAKE_ITEM(new ActionVariable(Integer.class, "amount", 1), new ActionVariable(Integer.class, "slot", 0)),
    CLEAR_INVENTORY,
    OPEN_MENU(new ActionVariable(String.class, "menuName", "")),
    SEND_MESSAGE(new ActionVariable(String.class, "message", "")),
    TELEPORT_LOCATION(),
    TELEPORT_PLAYER(new ActionVariable(EventPlayer.class, "player", null));

    private List<ActionVariable> variableList;

    ActionType(ActionVariable... variables){
        this.variableList = Arrays.asList(variables);
    }

    public List<ActionVariable> getVariables(){
        return variableList;
    }
}
