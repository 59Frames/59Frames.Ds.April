package util;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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

    @NotNull
    public static String randomUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
