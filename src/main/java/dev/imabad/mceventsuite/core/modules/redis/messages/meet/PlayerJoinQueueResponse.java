package dev.imabad.mceventsuite.core.modules.redis.messages.meet;

import dev.imabad.mceventsuite.core.modules.redis.RedisBaseMessage;

public class PlayerJoinQueueResponse extends RedisBaseMessage {
    private boolean success;
    private Failure failureReason;
    private String extraData;
    private boolean full;

    public PlayerJoinQueueResponse(boolean full, String extraData) {
        this.success = true;
        this.full = full;
        this.extraData = extraData;
    }

    public PlayerJoinQueueResponse(Failure failure) {
        this(failure, null);
    }

    public PlayerJoinQueueResponse(Failure failure, String extraData) {
        this.failureReason = failure;
        this.success = false;
        this.extraData = extraData;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Failure getFailureReason() {
        return this.failureReason;
    }

    public boolean isFull() {
        return this.full;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public enum Failure {
        ALREADY_IN_QUEUE, IN_OTHER_QUEUE, NO_SUCH_QUEUE;
    }
}
