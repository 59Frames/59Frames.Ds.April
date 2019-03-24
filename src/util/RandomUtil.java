package util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static double random() {
        return random(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static int random(final int bound) {
        return random(0, bound);
    }

    public static double random(final double bound) {
        return random(0, bound);
    }

    public static int random(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double random(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    @NotNull
    public static String randomUID() {
        return StringUtil.randomUID();
    }
}
