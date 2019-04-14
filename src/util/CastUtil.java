package util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.List;

/**
 * {@link CastUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class CastUtil {

    private CastUtil() {
    }

    public static <T> T cast(final Object o, Class<T> to) {
        return to.cast(o);
    }

    public static String str(final Object o) {
        return String.valueOf(o);
    }

    public static double num(final Object n) {
        return Double.parseDouble(str(n));
    }
}
