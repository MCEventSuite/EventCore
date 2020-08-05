package dev.imabad.mceventsuite.core.util;

import java.util.HashMap;

public class PropertyMap extends HashMap<String, Object> {

    public int getIntProperty(String name){
        if(this.get(name) instanceof Double){
            return ((Double) this.get(name)).intValue();
        }
        return (int) this.get(name);
    }

    public String getStringProperty(String name){
        return (String) this.get(name);
    }

    public boolean getBooleanProperty(String name){
        return (boolean) this.get(name);
    }

    public float getFloatProperty(String name){
        return (float) this.get(name);
    }

    public double getDoubleProperty(String name){
        return (double) this.get(name);
    }
}
