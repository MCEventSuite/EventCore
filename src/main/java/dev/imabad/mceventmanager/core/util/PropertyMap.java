package dev.imabad.mceventmanager.core.util;

import java.util.HashMap;

public class PropertyMap<K, V> extends HashMap {

    public int getIntProperty(String name){
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
