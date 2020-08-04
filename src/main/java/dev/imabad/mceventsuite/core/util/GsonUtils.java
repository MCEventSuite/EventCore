package dev.imabad.mceventsuite.core.util;

import com.google.gson.Gson;

public class GsonUtils {

    private static Gson gson;

    public static Gson getGson(){
        if(gson == null){
            gson = new Gson();
        }
        return gson;
    }
}
