package util;

import org.jetbrains.annotations.NotNull;

/**
 * {@link StringUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class StringUtil {

    @NotNull
    public static String clean(@NotNull String string) {
        string = string.trim().strip();
        string = string.replaceAll("\t", " ");
        string = string.replaceAll("\\s{2,}", " ");

        return string;
    }

    public static String timeString(final double milliseconds) {
        String result = "";

        int m = (int) milliseconds;

        int hours = 0;
        while (m >= 1000*60*60) {
            m -= 1000*60*60;
            hours++;
        }
        int minutes = 0;
        while (m >= 1000*60) {
            m -= 1000*60;
            minutes++;
        }
        if (hours > 0) {
            result += hours + " hours, ";
        }
        int seconds = 0;
        while (m >= 1000) {
            m -= 1000;
            seconds ++;
        }
        result += minutes + " minutes and ";
        result += seconds + " seconds.";
        return result;
    }

    @NotNull
    public static String randomUID() {
        return RandomUtil.randomUID();
    }
}
