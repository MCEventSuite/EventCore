package dev.imabad.mceventsuite.core.util;

import java.util.regex.Pattern;

public class ProfanityUtils {

    public static final String[] censoredWords = new String[] {"anal", "anus", "ass", "asshole",
            "ballsack", "bastard", "bich", "bitch", "bitcher", "boob", "butt", "clit", "coc",
            "cock", "cok", "cum", "cunt", "dic", "dick", "dik", "dildo", "douch", "dumb",
            "fag", "fap", "fc", "fk", "fq", "fu", "fuc", "fuck", "fudge", "fuk", "fuq", "gai",
            "gay", "horny", "idiot", "jerc", "jerk", "kum", "kunt", "lesbian", "lesbien",
            "niga", "niger", "orgie", "orgy", "penis", "pric", "prick", "pussy", "queer",
            "rape", "rectum", "sack", "sex", "shit", "slut", "sperm", "suck", "tit", "vagin",
            "whore",
    };
    private static final Pattern pattern;

    static {
        StringBuilder builder = new StringBuilder();
        for(String s : censoredWords){
            if(builder.length() != 0)
                builder.append("|");
            builder.append("(").append(s).append(")");
        }
        pattern = Pattern.compile(builder.toString());
    }

    public static boolean containsProfanity(String message) {
        return pattern.matcher(message).find();
    }

}
