package dev.imabad.mceventsuite.core.utils;

import dev.imabad.mceventsuite.core.util.ProfanityUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfanityUtilsTest {

    @Test
    public void matchesSwears(){
        boolean matches = false;
        System.out.println(ProfanityUtils.getPattern().toString());
        for(String s : ProfanityUtils.censoredWords){
            matches = ProfanityUtils.containsProfanity(s);
        }
        assertTrue(matches);
    }

}
