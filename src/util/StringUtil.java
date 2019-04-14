package util;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.stream.Stream;

/**
 * {@link StringUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class StringUtil {

    private StringUtil() {
    }

    @NotNull
    public static String clean(@NotNull String string) {
        string = string.trim().strip();
        string = string.replaceAll("\t", " ");
        string = string.replaceAll("\\s{2,}", " ");

        return string;
    }

    public static String timeString(final long milliseconds) {
        Duration duration = Duration.ofMillis(milliseconds);
        long days = duration.toDays();
        long years = days / 365;
        days = days % 365;
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.toSeconds();
        long millis = duration.toMillis();
        long nanos = duration.toNanos();

        return String.format("%d Years, %d days, %d hours, %d minutes, %d seconds, %d milliseconds and %d nanoseconds", years, days, hours, minutes, seconds, millis, nanos);
    }

    @NotNull
    public static String randomUID() {
        return RandomUtil.randomUID();
    }
}
