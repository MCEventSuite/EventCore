package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

import java.util.HashMap;

public class ServerAnnounceDisplayNames extends RedisBaseMessage {
    private HashMap<String, String> displayNames;

    public ServerAnnounceDisplayNames(HashMap<String, String> displayNames) {
        this.displayNames = displayNames;
    }

    public HashMap<String, String> getDisplayNames() {
        return this.displayNames;
    }
}
