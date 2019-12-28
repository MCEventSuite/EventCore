package dev.imabad.mceventsuite.core.api.actions;

public class ActionVariable {

    private Class type;
    private String name;
    private Object defaultValue;

    public ActionVariable(Class type, String name, Object defaultValue) {
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }


}
