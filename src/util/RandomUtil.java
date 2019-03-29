package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @NotNull
    public static String randomString() {
        return RandomString.randomString();
    }

    @NotNull
    public static String randomString(final int length) {
        return RandomString.randomString(length);
    }

    private final static class RandomString {

        private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
        private static final String DIGITS = "0123456789";
        private static final String ALPHA_NUM = UPPER + LOWER + DIGITS;
        private final Random RANDOM;
        private final char[] SYMBOLS;
        private final char[] BUFFER;

        private RandomString() {
            this.RANDOM = ThreadLocalRandom.current();
            this.SYMBOLS = ALPHA_NUM.toCharArray();
            this.BUFFER = new char[8];
        }

        /**
         * Constructs a new instance of type {@link util.RandomUtil.RandomString}
         *
         * @param length    - the length of the desired string
         */
        private RandomString(int length) {
            if (length < 1) throw new IllegalArgumentException();
            this.RANDOM = ThreadLocalRandom.current();
            this.SYMBOLS = ALPHA_NUM.toCharArray();
            this.BUFFER = new char[length];
        }

        /**
         * Constructs a new instance of type {@link util.RandomUtil.RandomString}
         *
         * @param length    - the length of the desired string
         * @param random    - the random to pick chars
         * @param symbols   - the symbols to pick chars from
         */
        private RandomString(int length, Random random, String symbols) {
            if (length < 1) throw new IllegalArgumentException();
            if (symbols.length() < 2) throw new IllegalArgumentException();
            this.RANDOM = Objects.requireNonNull(random);
            this.SYMBOLS = symbols.toCharArray();
            this.BUFFER = new char[length];
        }

        /**
         * Generate a random string.
         */
        @Contract(" -> new")
        @NotNull
        private String nextString() {
            for (int idx = 0; idx < BUFFER.length; ++idx)
                BUFFER[idx] = SYMBOLS[RANDOM.nextInt(SYMBOLS.length)];
            return new String(BUFFER);
        }

        @NotNull
        private static String randomString() {
            return new RandomString().nextString();
        }

        /**
         * @param length - String length
         * @return random String
         */
        @NotNull
        private static String randomString(int length) {
            return new RandomString(length).nextString();
        }
    }
}
