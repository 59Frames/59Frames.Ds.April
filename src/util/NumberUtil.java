package util;

/**
 * {@link NumberUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class NumberUtil {

    private NumberUtil() {
    }

    public static float map(float value, float iStart, float iStop, float oStart, float oStop) {
        return oStart + (oStop - oStart) * ((value - iStart) / (iStop - iStart));
    }

}
