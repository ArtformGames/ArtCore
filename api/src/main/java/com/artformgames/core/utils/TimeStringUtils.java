package com.artformgames.core.utils;

import org.jetbrains.annotations.NotNull;

public class TimeStringUtils {

    private TimeStringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static long toMilliSec(String s) {
        String[] sl = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

        long i = Long.parseLong(sl[0]);
        try {
            return switch (sl[1]) {
                case "s" -> (i * 1000);
                case "m" -> (i * 1000 * 60);
                case "h" -> (i * 1000 * 60 * 60);
                case "d" -> (i * 1000 * 60 * 60 * 24);
                case "w" -> (i * 1000 * 60 * 60 * 24 * 7);
                case "mo" -> (i * 1000 * 60 * 60 * 24 * 30);
                case "y" -> (i * 1000 * 60 * 60 * 24 * 30 * 12);
                default -> -1;
            };
        } catch (Exception e) {
            return -1;
        }
    }

    public static long toMilliSecPlus(String s) {
        String[] ss = s.split(",");
        long l = 0;
        for (String temp : ss) {
            long value = toMilliSec(temp);
            if (value == -1) return -1;
            l += value;
        }
        return l;
    }


    public static @NotNull String toTimeString(long millis) {
        long allSeconds = millis / 1000;
        if (allSeconds < 1) return "0s";

        long days = allSeconds / 86400L;
        long hours = allSeconds % 86400L / 3600L;
        long minutes = allSeconds % 3600L / 60L;
        long seconds = allSeconds % 60L;
        String dateString;
        if (days > 0L) {
            dateString = days + "d" + (hours > 0L ? "," + hours + "h" : "") + (minutes > 0L ? "," + minutes + "," : "") + (seconds > 0L ? "," + seconds + "s" : "");
        } else if (hours > 0L) {
            dateString = hours + "h" + (minutes > 0L ? "," + minutes + "m" : "") + (seconds > 0L ? "," + seconds + "s" : "");
        } else if (minutes > 0L) {
            dateString = minutes + "m" + (seconds > 0L ? "," + seconds + "s" : "");
        } else {
            dateString = seconds + "s";
        }
        return dateString;
    }

}
