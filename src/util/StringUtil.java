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
    public static String clean(@NotNull String string) {
        return string.trim().strip();
    }
}
