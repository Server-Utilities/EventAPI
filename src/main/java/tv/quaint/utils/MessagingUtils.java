package tv.quaint.utils;

import tv.quaint.EventAPI;

public class MessagingUtils {
    public static void info(String string) {
        EventAPI.LOGGER.info(string);
    }

    public static void warn(String string) {
        EventAPI.LOGGER.warn(string);
    }

    public static void severe(String string) {
        EventAPI.LOGGER.error(string);
    }
}
