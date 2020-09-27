package dev.imabad.mceventsuite.core.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    /**
     * Converts a milliseconds duration into a String formatted in days, hours, minutes and seconds
     *
     * @param duration - Duration in milliseconds
     * @return Duration formatted as a string
     */
    public static String getFormattedDuration(long duration){
        if(duration < 0){
            throw new IllegalArgumentException("Duration must be more than 0");
        }
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        duration -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        duration -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        duration -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);

        StringBuilder stringBuilder = new StringBuilder();
        if(days > 0){
            stringBuilder.append(days);
            stringBuilder.append(" days ");
        }
        if(hours > 0){
            stringBuilder.append(hours);
            stringBuilder.append(" hours ");
        }
        if(minutes > 0){
            stringBuilder.append(minutes);
            stringBuilder.append(" minutes ");
        }
        if(seconds > 0){
            stringBuilder.append(seconds);
            stringBuilder.append(" seconds ");
        }
        return stringBuilder.toString();
    }

    /**
     * Returns a duration from a String
     * Example String: 1d2h5m
     * Example String 2: 2h
     * @param input The input string
     * @return      The duration in milliseconds
     */
    public static long getDurationFromString(String input){
        input = input.toLowerCase();
        long duration = 0;
        int lastLetter = 0;
        char[] chars = input.toCharArray();
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            if(Character.isLetter(c)){
                String amountString = input.substring(lastLetter, i);
                int amount = Integer.parseInt(amountString);
                TimeUnit timeUnit = unitFromCharacter(c);
                if(timeUnit == null){
                    throw new IllegalArgumentException("invalid time unit");
                }
                duration += timeUnit.toMillis(amount);
            }
        }
        return duration;
    }

    private static TimeUnit unitFromCharacter(char character){
        switch(character){
            case 'd':
                return TimeUnit.DAYS;
            case 'h':
                return TimeUnit.HOURS;
            case 'm':
                return TimeUnit.MINUTES;
            case 's':
                return TimeUnit.SECONDS;
        }
        return null;
    }

}
