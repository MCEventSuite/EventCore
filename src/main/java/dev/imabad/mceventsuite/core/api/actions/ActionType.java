package dev.imabad.mceventsuite.core.api.actions;

import dev.imabad.mceventsuite.core.api.player.ILocation;
import dev.imabad.mceventsuite.core.api.player.IPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ActionType {

    GIVE_ITEM(new ActionVariable(Integer.class, "amount", 1), new ActionVariable(String.class, "type", "minecraft:stone"), new ActionVariable(Integer.class, "slot", -1), new ActionVariable(String.class, "name", ""), new ActionVariable(List.class, "lore", new ArrayList<>())),
    TAKE_ITEM(new ActionVariable(Integer.class, "amount", 1), new ActionVariable(Integer.class, "slot", 0)),
    CLEAR_INVENTORY,
    OPEN_MENU(new ActionVariable(String.class, "menuName", "")),
    SEND_MESSAGE(new ActionVariable(String.class, "message", "")),
    TELEPORT_LOCATION(new ActionVariable(ILocation.class, "location", null)),
    TELEPORT_PLAYER(new ActionVariable(IPlayer.class, "player", null));

    private List<ActionVariable> variableList;

    ActionType(ActionVariable... variables){
        this.variableList = Arrays.asList(variables);
    }

    public List<ActionVariable> getVariables(){
        return variableList;
    }
}
