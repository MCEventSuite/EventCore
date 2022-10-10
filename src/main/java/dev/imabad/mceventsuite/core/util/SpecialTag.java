package dev.imabad.mceventsuite.core.util;

public enum SpecialTag {
    CHAT(0x0316, 0xE039),
    DEV(0x0301, 0xE031),
    FOUNDER(0x0302, 0xE033),
    AFK(0x0303, 0xE030),
    EXHIBITOR(0x0304, 0xE032),
    GAMEMASTER(0x0305, 0xE034),
    GUEST(0x0306, 0xE035),
    HEAD(0x0307, 0xE036),
    LIVESTREAM(0x0308, 0xE037),
    MANAGER(0x0309, 0xE038),
    MOD(0x0310, 0xE019),
    SPONSOR(0x0311, 0xE01A),
    SRMOD(0x0312, 0xE01B),
    STICKYPISTON(0x0313, 0xE01C),
    VIP(0x0314, 0xE01D),
    VIPP(0x0315, 0xE01E),

    //BOSSBAR
    NORTH(0x0317),
    EAST(0x0318),
    SOUTH(0x0319),
    WEST(0x0320),
    NORTHEAST(0x0321),
    SOUTHEAST(0x0322),
    SOUTHWEST(0x0323),
    NORTHWEST(0x0324),

    LOCATION_PIN(0x0333),
    CLOCK(0x0334);

    private char java;
    private char[] bedrock;

    SpecialTag(char java, char... bedrock) {
        this.java = java;
        this.bedrock = bedrock;
    }

    SpecialTag(int java, int... bedrock) {
        this.java = Character.toChars(java)[0];
        this.bedrock = new char[bedrock.length];
        for(int i = 0; i < bedrock.length; i++) {
            this.bedrock[i] = Character.toChars(bedrock[i])[0];
        }
    }

    public char getJava() {
        return this.java;
    }

    public String getJavaString() {
        return String.valueOf(this.java);
    }

    public char[] getBedrock() {
        return this.bedrock;
    }

    public String getBedrockString() {
        if(this.bedrock == null || this.bedrock.length == 0) {
            return "[" + this.name() + "]";
        }
        return String.valueOf(this.bedrock);
    }
}
