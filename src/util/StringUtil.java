package util;

import org.jetbrains.annotations.NotNull;

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
}
