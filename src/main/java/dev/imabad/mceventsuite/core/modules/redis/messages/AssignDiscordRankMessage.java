package dev.imabad.mceventsuite.core.modules.redis.messages;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class AssignDiscordRankMessage extends RedisBaseMessage {

    private String discordName;
    private String rankID;

    public AssignDiscordRankMessage(String discordName, String rankID){
        this.discordName = discordName;
        this.rankID = rankID;
    }

    public String getDiscordName() {
        return discordName;
    }

    public String getRankID() {
        return rankID;
    }
}
