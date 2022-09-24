package dev.imabad.mceventsuite.core.util;

public enum SpecialTag {
    CHAT(0x0316),
    DEV(0x0301),
    FOUNDER(0x0302),
    AFK(0x0303),
    EXHIBITOR(0x0304),
    GAMEMASTER(0x0305),
    GUEST(0x0306),
    HEAD(0x0307),
    LIVESTREAM(0x0308),
    MANAGER(0x0309),
    MOD(0x0310),
    SPONSOR(0x0311),
    SRMOD(0x0312),
    STICKYPISTON(0x0313),
    VIP(0x0314),
    VIPP(0x0315);

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
        return String.valueOf(this.bedrock);
    }
}
