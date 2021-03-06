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

    public static boolean like(@NotNull final String string, @NotNull final String like) {
        if (like.startsWith("%") && like.endsWith("%")) {
            return string.contains(like.substring(1, like.lastIndexOf("%")));
        } else if (like.startsWith("%") && !like.endsWith("%")) {
            return string.endsWith(like.substring(1));
        } else if (!like.startsWith("%") && like.endsWith("%")) {
            return string.startsWith(like.substring(0, like.lastIndexOf("%")));
        }
        
        return false;
    }
}
