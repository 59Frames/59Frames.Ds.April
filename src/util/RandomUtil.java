package util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static double random() {
        return random(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static int random(int bound) {
        return random(0, bound);
    }

    public static double random(double bound) {
        return random(0, bound);
    }

    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
