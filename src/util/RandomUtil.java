package util;

import org.jetbrains.annotations.NotNull;

public class RandomUtil {
    public static double random() {
        return Silvester.random();
    }

    public static int random(final int bound) {
        return Silvester.randomInt(bound);
    }

    public static double random(final double bound) {
        return Silvester.random(bound);
    }

    public static int random(final int min, final int max) {
        return Silvester.randomInt(min, max);
    }

    public static double random(final double min, final double max) {
        return Silvester.random(min, max);
    }

    @NotNull
    public static String randomUID() {
        return StringUtil.randomUID();
    }
}
