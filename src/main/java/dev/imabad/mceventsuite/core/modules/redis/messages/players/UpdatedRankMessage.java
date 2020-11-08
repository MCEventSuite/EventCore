package dev.imabad.mceventsuite.core.modules.redis.messages.players;

import dev.imabad.mceventsuite.core.api.objects.EventRank;
import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class UpdatedRankMessage extends RedisBaseMessage {

    private EventRank rank;

    public UpdatedRankMessage(EventRank rank) {
        this.rank = rank;
    }

    public EventRank getRank() {
        return rank;
    }
}
