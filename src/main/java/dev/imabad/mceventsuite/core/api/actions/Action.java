package dev.imabad.mceventsuite.core.api.actions;

import dev.morphia.annotations.Entity;

import java.util.HashMap;
import java.util.stream.Collectors;

@Entity
public class Action {

    private ActionType type;
    private HashMap<String, Object> variables;

    protected Action(ActionType type){
        this.type = type;
        type.getVariables().forEach(variable -> {
            variables.put(variable.getName(), variable.getDefaultValue());
        });
    }

    protected void setVariable(ActionVariable actionVariable, Object value){
        this.variables.put(actionVariable.getName(), value);
    }

    public Object getVariable(String name){
        ActionVariable actionVariable = this.type.getVariables().stream().filter(variable -> variable.getName().equals(name)).collect(Collectors.toList()).get(0);
        if(actionVariable == null){
            return null;
        }
        return actionVariable.getType().cast(this.variables.get(name));
    }

    public HashMap<String, Object> getVariables(){
        return this.variables;
    }

    public ActionType getType() {
        return type;
    }
}
