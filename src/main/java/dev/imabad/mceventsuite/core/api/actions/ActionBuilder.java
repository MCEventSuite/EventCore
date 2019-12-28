package dev.imabad.mceventsuite.core.api.actions;

public class ActionBuilder {

    private Action action;

    public ActionBuilder(ActionType type){
        this.action = new Action(type);
    }

    public ActionBuilder setVariable(ActionVariable variable, Object value){
        this.action.setVariable(variable, value);
        return this;
    }

    public Action build(){
        return this.action;
    }

}
