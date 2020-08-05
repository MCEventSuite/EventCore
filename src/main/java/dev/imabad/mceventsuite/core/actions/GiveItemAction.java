package dev.imabad.mceventsuite.core.actions;

import dev.imabad.mceventsuite.core.api.actions.Action;
import dev.imabad.mceventsuite.core.api.actions.ActionType;
import dev.imabad.mceventsuite.core.api.actions.ActionVariable;

import java.util.Collections;
import java.util.List;

public class GiveItemAction {
    public static final ActionVariable MATERIAL = new ActionVariable(String.class, "material", "minecraft:air");
    public static final ActionVariable AMOUNT = new ActionVariable(Integer.class, "amount", 1);
    public static final ActionVariable NAME = new ActionVariable(String.class, "name", "");
    public static final ActionVariable SLOT = new ActionVariable(Integer.class, "slot", -1);
    public static final ActionVariable LORE = new ActionVariable(List.class, "lore", Collections.emptyList());
}
