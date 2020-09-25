package dev.imabad.mceventsuite.core.modules.ac;

public class AccessControlResponse {

    public static AccessControlResponse ALLOWED = new AccessControlResponse(true);
    public static AccessControlResponse DENIED = new AccessControlResponse(false);

    private boolean allowed;
    private String denyReason;

    public AccessControlResponse(boolean allowed) {
        this.allowed = allowed;
        this.denyReason = "You cannot access this at this time";
    }

    public AccessControlResponse(boolean allowed, String denyReason) {
        this.allowed = allowed;
        this.denyReason = denyReason;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getDenyReason() {
        return denyReason;
    }
}
