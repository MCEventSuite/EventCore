package dev.imabad.mceventsuite.core.util;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

public class UUIDUtils {

    public static String insertDashUUID(String uuid) {
        StringBuffer idBuff = new StringBuffer(uuid);
        idBuff.insert(20, '-');
        idBuff.insert(16, '-');
        idBuff.insert(12, '-');
        idBuff.insert(8, '-');
        return idBuff.toString();
    }


    public static UUID getFromUsername(String username){
        try {
            URL url = new URL("https://api.mymcuu.id/username/" + username);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Cubed2020");
            int status = con.getResponseCode();
            if(status != 200){
                return null;
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            JsonObject jsonObject = GsonUtils.getGson().fromJson(in, JsonObject.class);
            if(jsonObject.has("uuid")){
                return UUID.fromString(jsonObject.get("uuid").getAsString());
            }
            in.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
