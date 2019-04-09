package util;

/**
 * {@link CastUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class CastUtil {
    public static String str(final Object o) {
        return String.valueOf(o);
    }

    public static double num(final Object n) {
        return Double.parseDouble(str(n));
    }
}
