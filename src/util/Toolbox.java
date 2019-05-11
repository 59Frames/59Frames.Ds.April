package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * {@link Toolbox}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@SuppressWarnings("unused")
public final class Toolbox {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final double E = 2.71828182845904523536028747135266249D; // e, Napier's constant, or Euler's number
    public static final double Y = 0.57721566490153286060651209008240243D; // Euler-Mascheroni-Constant
    public static final double PI = 3.1415926535897932384626433832795028841971693993751058209749445923078164D; // Pi, Archimedes' constant or Ludolph's number
    public static final double HALF_PI = PI / 2; // Half Pi
    public static final double TWO_PI = PI * 2; // Two times Pi
    public static final double ROOT_2 = 1.41421356237309504880168872420969807D; // Pythagoras' constant, square root of 2
    public static final double ROOT_3 = 1.73205080756887729352744634150587236D; // Theodorus' constant, square root of 3
    public static final double ROOT_5 = 2.23606797749978969640917366873127623D; // square root of 5
    public static final double GOLDEN_RATIO = 1.61803398874989484820458683436563811D; // Golden Ration

    private static Toolbox.PerlinNoiseGenerator generator = new Toolbox.PerlinNoiseGenerator();

    @Contract(pure = true)
    private Toolbox() {
    }

    @Contract(pure = true)
    public static double map(final double value, final double iStart, final double iStop, final double oStart, final double oStop) {
        return oStart + (oStop - oStart) * ((value - iStart) / (iStop - iStart));
    }

    public static double random() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double random(final double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }

    public static double random(final double a, final double b) {
        final double max = max(a, b);
        final double min = min(a, b);

        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int randomInt(int a, int b) {
        final int min = min(a, b);
        final int max = max(a, b);
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static synchronized void noiseSeed(final int seed) {
        generator = new Toolbox.PerlinNoiseGenerator(seed);
    }

    public static double improvedNoise(final double x, final double y, final double z) {
        return generator.improvedNoise(x, y, z);
    }

    public static double noise(final float x) {
        return generator.noise(x);
    }

    public static double noise(final float x, final float y) {
        return generator.noise(x, y);
    }

    public static double noise(final float x, final float y, final float z) {
        return generator.noise(x, y, z);
    }

    public static double improvedTurbulence(final double x, final double y, final double z, final float loF, final float hiF) {
        return generator.improvedTurbulence(x, y, z, loF, hiF);
    }

    @Contract(pure = true)
    public static int round(final float n) {
        return Math.round(n);
    }

    @Contract(pure = true)
    public static long round(final double n) {
        return Math.round(n);
    }

    public static double round(final double value, int fractionalPlaces) {
        if (fractionalPlaces < 0)
            fractionalPlaces = 0;

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(fractionalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float round(final float value, int fractionalPlaces) {
        if (fractionalPlaces < 0)
            fractionalPlaces = 0;

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(fractionalPlaces, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Contract(pure = true)
    public static long max(final long a, final long b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static int max(final int a, final int b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static double max(final double a, final double b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static float max(final float a, final float b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static <T extends Comparable<T>> T max(@NotNull final T a, @NotNull final T b) {
        return (a.compareTo(b) >= 0) ? a : b;
    }

    @Contract(pure = true)
    public static double max(final Number[] col) {
        return max(CollectionUtil.asList(col));
    }

    @Contract(pure = true)
    public static double max(final List<? extends Number> col) {
        double result = Double.MIN_VALUE;
        for (Number n : col) {
            if (n.doubleValue() > result)
                result = n.doubleValue();
        }
        return result;
    }

    @Contract(pure = true)
    public static long min(final long a, final long b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static int min(final int a, final int b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static double min(final double a, final double b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static float min(final float a, final float b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static <T extends Comparable<T>> T min(@NotNull final T a, @NotNull final T b) {
        return (a.compareTo(b) <= 0) ? a : b;
    }

    @Contract(pure = true)
    public static double min(final Number[] col) {
        return min(CollectionUtil.asList(col));
    }

    @Contract(pure = true)
    public static double min(final List<? extends Number> col) {
        double result = Double.MAX_VALUE;
        for (Number n : col) {
            if (n.doubleValue() < result)
                result = n.doubleValue();
        }
        return result;
    }

    @Contract(pure = true)
    public static double sum(final List<? extends Number> col) {
        double result = 0.0;

        for (Number n : col)
            result += n.doubleValue();

        return result;
    }

    @Contract(pure = true)
    public static double sum(final Number[] col) {
        return sum(CollectionUtil.asList(col));
    }

    @Contract(pure = true)
    public static double copySign(final double magnitude, final double sign) {
        return Math.copySign(magnitude, sign);
    }

    @Contract(pure = true)
    public static float copySign(final float magnitude, final float sign) {
        return Math.copySign(magnitude, sign);
    }

    public static double pow(final double a, final double b) {
        return StrictMath.pow(a, b);
    }

    @Contract(pure = true)
    public static double average(@NotNull final int[] arr) {
        double sum = 0.0;

        for (int n : arr)
            sum += n;

        return (sum / arr.length);
    }

    @Contract(pure = true)
    public static double average(@NotNull final long[] arr) {
        double sum = 0.0;

        for (long n : arr)
            sum += n;

        return (sum / arr.length);
    }

    @Contract(pure = true)
    public static double average(@NotNull final double[] arr) {
        double sum = 0.0;

        for (double n : arr)
            sum += n;

        return (sum / arr.length);
    }

    @Contract(pure = true)
    public static double average(@NotNull final float[] arr) {
        double sum = 0.0;

        for (float n : arr)
            sum += n;

        return (sum / arr.length);
    }

    public static double average(@NotNull final Number[] list) {
        double sum = 0.0;
        if (list.length == 0)
            return sum;

        for (Number n : list)
            sum += n.doubleValue();

        return (sum / list.length);
    }

    public static double average(@NotNull final List<? extends Number> list) {
        double sum = 0.0;
        if (list.isEmpty())
            return sum;

        for (Number n : list)
            sum += n.doubleValue();

        return (sum / list.size());
    }

    public static double average(@NotNull final Map<?, ? extends Number> map) {
        final double[] sum = new double[1];
        sum[0] = 0.0;
        if (map.isEmpty())
            return sum[0];

        map.forEach((key, value) -> sum[0] += value.doubleValue());

        return (sum[0] / map.size());
    }

    public static double median(@NotNull List<? extends Number> values) {
        values = values.parallelStream().sorted((Comparator<Number>) (o1, o2) -> {
            Double d1 = (o1 == null) ? Double.POSITIVE_INFINITY : o1.doubleValue();
            Double d2 = (o2 == null) ? Double.POSITIVE_INFINITY : o2.doubleValue();
            return d1.compareTo(d2);
        }).collect(Collectors.toList());

        int mid = values.size() / 2;
        return values.size() % 2 == 1
                ? values.get(mid).doubleValue()
                : (values.get(mid - 1).doubleValue() + values.get(mid).doubleValue()) / 2;
    }

    @Contract(pure = true)
    public static double exp(final double d) {
        return StrictMath.exp(d);
    }

    @Contract(pure = true)
    public static double volume(final double width, final double length, final double height) {
        return width * length * height;
    }

    @Contract(pure = true)
    public static double surface(final double width, final double length) {
        return width * length;
    }

    public static double cylinderSurface(final double radius, final double height) {
        return (circular(radius) * 2) + (circumference(radius) * height);
    }

    public static double cylinderVolume(final double radius, double height) {
        return circular(radius) * height;
    }

    @Contract(pure = true)
    public static double circumference(final double radius) {
        return (radius * 2) * PI;
    }

    public static double circular(final double radius) {
        return (pow(radius, 2)) * PI;
    }

    public static double rectangleSurface(final double width, final double length, final double height) {
        return ((surface(width, length) * 2) + (surface(width, height) * 2) + (surface(length, height) * 2));
    }

    @Contract(pure = true)
    public static double rectangleVolume(final double width, final double length, final double height) {
        return volume(width, length, height);
    }

    @Contract(pure = true)
    public static double kmh2mph(final double kmh) {
        return (kmh / 1.609);
    }

    @Contract(pure = true)
    public static double mph2kmh(final double mph) {
        return (mph * 1.609);
    }

    @Contract(pure = true)
    public static double mph2knots(final double mph) {
        return (mph / 1.151);
    }

    @Contract(pure = true)
    public static double knots2mph(final double knots) {
        return (knots * 1.151);
    }

    @Contract(pure = true)
    public static double kmh2knots(final double kmh) {
        return (kmh / 1.852);
    }

    @Contract(pure = true)
    public static double knots2kmh(final double knots) {
        return (knots * 1.852);
    }

    @Contract(pure = true)
    public static double kmh2mps(final double kmh) {
        return (kmh / 3.6);
    }

    @Contract(pure = true)
    public static double mps2kmh(final double mps) {
        return (mps * 3.6);
    }

    @Contract(pure = true)
    public static double mph2mps(final double mph) {
        return (mph / 2.237);
    }

    @Contract(pure = true)
    public static double mps2mph(final double mps) {
        return (mps * 2.237);
    }

    @Contract(pure = true)
    public static double celsius2fahrenheit(final double celsius) {
        return ((celsius * 9 / 5) + 32);
    }

    @Contract(pure = true)
    public static double celsius2kelvin(final double celsius) {
        return (celsius + 273.15);
    }

    public static double fahrenheit2kelvin(final double fahrenheit) {
        return celsius2kelvin(fahrenheit2celsius(fahrenheit));
    }

    @Contract(pure = true)
    public static double fahrenheit2celsius(final double fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    @Contract(pure = true)
    public static double kelvin2celsius(final double kelvin) {
        return (kelvin - 273.15);
    }

    public static double kelvin2fahrenheit(final double kelvin) {
        return celsius2fahrenheit(kelvin2celsius(kelvin));
    }

    @NotNull
    public static String randomUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @NotNull
    public static String randomString() {
        return Toolbox.RandomString.randomString();
    }

    @NotNull
    public static String randomString(final int length) {
        return Toolbox.RandomString.randomString(length);
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

    public static int integer(final Object n) {
        return round(Float.parseFloat(str(n)));
    }

    public static boolean bool(@NotNull final String val) {
        return Boolean.parseBoolean(val);
    }

    public static <T> T[] asArray(@NotNull final List<T> list) {
        return CollectionUtil.asArray(list);
    }

    public static <T> List<T> asList(@NotNull final T[] arr) {
        return CollectionUtil.asList(arr);
    }

    public static void sleep(final long timeMilliseconds) {
        try {
            Thread.sleep(timeMilliseconds);
        } catch (InterruptedException e) {
            Debugger.exception(e);
        }
    }

    private static final class RandomString {

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
         * Constructs a new instance of type {@link util.Toolbox.RandomString}
         *
         * @param length - the length of the desired string
         */
        private RandomString(int length) {
            if (length < 1) throw new IllegalArgumentException();
            this.RANDOM = ThreadLocalRandom.current();
            this.SYMBOLS = ALPHA_NUM.toCharArray();
            this.BUFFER = new char[length];
        }

        /**
         * Constructs a new instance of type {@link util.Toolbox.RandomString}
         *
         * @param length  - the length of the desired string
         * @param random  - the random to pick chars
         * @param symbols - the symbols to pick chars from
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
            return new Toolbox.RandomString().nextString();
        }

        /**
         * @param length - String length
         * @return random String
         */
        @NotNull
        private static String randomString(int length) {
            return new Toolbox.RandomString(length).nextString();
        }
    }

    private static final class PerlinNoiseGenerator {
        // Constants for setting up the Perlin-1 noise functions
        private static final int B = 0x1000;
        private static final int BM = 0xff;

        private static final int N = 0x1000;
        private static final int NP = 12;   /* 2^N */
        private static final int NM = 0xfff;

        /**
         * Default seed to use for the random number generation
         */
        private static final int DEFAULT_SEED = 100;

        /**
         * Default sample size to work with
         */
        private static final int DEFAULT_SAMPLE_SIZE = 256;

        /**
         * The log of 1/2 constant. Used Everywhere
         */
        private static final float LOG_HALF = (float) Math.log(0.5);

        /**
         * Permutation array for the improved noise function
         */
        private int[] p_imp;

        /**
         * P array for perline 1 noise
         */
        private int[] p;
        private float[][] g3;
        private float[][] g2;
        private float[] g1;


        /**
         * Create a new noise creator with the default seed value
         */
        private PerlinNoiseGenerator() {
            this(DEFAULT_SEED);
        }

        /**
         * Create a new noise creator with the given seed value for the randomness
         *
         * @param seed The seed value to use
         */
        private PerlinNoiseGenerator(int seed) {
            p_imp = new int[DEFAULT_SAMPLE_SIZE << 1];

            int i, j, k;
            Random rand = new Random(seed);

            // Calculate the table of psuedo-random coefficients.
            for (i = 0; i < DEFAULT_SAMPLE_SIZE; i++)
                p_imp[i] = i;

            // generate the psuedo-random permutation table.
            while (--i > 0) {
                k = p_imp[i];
                j = (int) (rand.nextLong() & DEFAULT_SAMPLE_SIZE);
                p_imp[i] = p_imp[j];
                p_imp[j] = k;
            }

            initPerlin1();
        }

        /**
         * Computes noise function for three dimensions at the point (x,y,z).
         *
         * @param x x dimension parameter
         * @param y y dimension parameter
         * @param z z dimension parameter
         * @return the noise value at the point (x, y, z)
         */
        private double improvedNoise(double x, double y, double z) {
            // Constraint the point to a unit cube
            int uc_x = (int) Math.floor(x) & 255;
            int uc_y = (int) Math.floor(y) & 255;
            int uc_z = (int) Math.floor(z) & 255;

            // Relative location of the point in the unit cube
            double xo = x - Math.floor(x);
            double yo = y - Math.floor(y);
            double zo = z - Math.floor(z);

            // Fade curves for x, y and z
            double u = fade(xo);
            double v = fade(yo);
            double w = fade(zo);

            // Generate a hash for each coordinate to find out where in the cube
            // it lies.
            int a = p_imp[uc_x] + uc_y;
            int aa = p_imp[a] + uc_z;
            int ab = p_imp[a + 1] + uc_z;

            int b = p_imp[uc_x + 1] + uc_y;
            int ba = p_imp[b] + uc_z;
            int bb = p_imp[b + 1] + uc_z;

            // blend results from the 8 corners based on the noise function
            double c1 = grad(p_imp[aa], xo, yo, zo);
            double c2 = grad(p_imp[ba], xo - 1, yo, zo);
            double c3 = grad(p_imp[ab], xo, yo - 1, zo);
            double c4 = grad(p_imp[bb], xo - 1, yo - 1, zo);
            double c5 = grad(p_imp[aa + 1], xo, yo, zo - 1);
            double c6 = grad(p_imp[ba + 1], xo - 1, yo, zo - 1);
            double c7 = grad(p_imp[ab + 1], xo, yo - 1, zo - 1);
            double c8 = grad(p_imp[bb + 1], xo - 1, yo - 1, zo - 1);

            return lerp(w, lerp(v, lerp(u, c1, c2), lerp(u, c3, c4)),
                    lerp(v, lerp(u, c5, c6), lerp(u, c7, c8)));
        }

        /**
         * 1-D noise generation function using the original perlin algorithm.
         *
         * @param x Seed for the noise function
         * @return The noisy output
         */
        private float noise(float x) {
            float t = x + N;
            int bx0 = ((int) t) & BM;
            int bx1 = (bx0 + 1) & BM;
            float rx0 = t - (int) t;
            float rx1 = rx0 - 1;

            float sx = sCurve(rx0);

            float u = rx0 * g1[p[bx0]];
            float v = rx1 * g1[p[bx1]];

            return lerp(sx, u, v);
        }

        /**
         * Create noise in a 2D space using the orignal perlin noise algorithm.
         *
         * @param x The X coordinate of the location to sample
         * @param y The Y coordinate of the location to sample
         * @return A noisy value at the given position
         */
        private float noise(float x, float y) {
            float t = x + N;
            int bx0 = ((int) t) & BM;
            int bx1 = (bx0 + 1) & BM;
            float rx0 = t - (int) t;
            float rx1 = rx0 - 1;

            t = y + N;
            int by0 = ((int) t) & BM;
            int by1 = (by0 + 1) & BM;
            float ry0 = t - (int) t;
            float ry1 = ry0 - 1;

            int i = p[bx0];
            int j = p[bx1];

            int b00 = p[i + by0];
            int b10 = p[j + by0];
            int b01 = p[i + by1];
            int b11 = p[j + by1];

            float sx = sCurve(rx0);
            float sy = sCurve(ry0);

            float[] q = g2[b00];
            float u = rx0 * q[0] + ry0 * q[1];
            q = g2[b10];
            float v = rx1 * q[0] + ry0 * q[1];
            float a = lerp(sx, u, v);

            q = g2[b01];
            u = rx0 * q[0] + ry1 * q[1];
            q = g2[b11];
            v = rx1 * q[0] + ry1 * q[1];
            float b = lerp(sx, u, v);

            return lerp(sy, a, b);
        }

        /**
         * Create noise in a 3D space using the orignal perlin noise algorithm.
         *
         * @param x The X coordinate of the location to sample
         * @param y The Y coordinate of the location to sample
         * @param z The Z coordinate of the location to sample
         * @return A noisy value at the given position
         */
        private float noise(float x, float y, float z) {
            float t = x + (float) N;
            int bx0 = ((int) t) & BM;
            int bx1 = (bx0 + 1) & BM;
            float rx0 = (float) (t - (int) t);
            float rx1 = rx0 - 1;

            t = y + (float) N;
            int by0 = ((int) t) & BM;
            int by1 = (by0 + 1) & BM;
            float ry0 = (float) (t - (int) t);
            float ry1 = ry0 - 1;

            t = z + (float) N;
            int bz0 = ((int) t) & BM;
            int bz1 = (bz0 + 1) & BM;
            float rz0 = (float) (t - (int) t);
            float rz1 = rz0 - 1;

            int i = p[bx0];
            int j = p[bx1];

            int b00 = p[i + by0];
            int b10 = p[j + by0];
            int b01 = p[i + by1];
            int b11 = p[j + by1];

            t = sCurve(rx0);
            float sy = sCurve(ry0);
            float sz = sCurve(rz0);

            float[] q = g3[b00 + bz0];
            float u = (rx0 * q[0] + ry0 * q[1] + rz0 * q[2]);
            q = g3[b10 + bz0];
            float v = (rx1 * q[0] + ry0 * q[1] + rz0 * q[2]);
            float a = lerp(t, u, v);

            q = g3[b01 + bz0];
            u = (rx0 * q[0] + ry1 * q[1] + rz0 * q[2]);
            q = g3[b11 + bz0];
            v = (rx1 * q[0] + ry1 * q[1] + rz0 * q[2]);
            float b = lerp(t, u, v);

            float c = lerp(sy, a, b);

            q = g3[b00 + bz1];
            u = (rx0 * q[0] + ry0 * q[1] + rz1 * q[2]);
            q = g3[b10 + bz1];
            v = (rx1 * q[0] + ry0 * q[1] + rz1 * q[2]);
            a = lerp(t, u, v);

            q = g3[b01 + bz1];
            u = (rx0 * q[0] + ry1 * q[1] + rz1 * q[2]);
            q = g3[b11 + bz1];
            v = (rx1 * q[0] + ry1 * q[1] + rz1 * q[2]);
            b = lerp(t, u, v);

            float d = lerp(sy, a, b);

            return lerp(sz, c, d);
        }

        /**
         * Create a turbulent noise output based on the core noise function. This
         * uses the noise as a base function and is suitable for creating clouds,
         * marble and explosion effects. For example, a typical marble effect would
         * set the colour to be:
         * <pre>
         *    sin(point + turbulence(point) * point.x);
         * </pre>
         */
        private double improvedTurbulence(double x, double y, double z, float loF, float hiF) {
            double p_x = x + 123.456f;
            double p_y = y;
            double p_z = z;
            double t = 0;
            double f;

            for (f = loF; f < hiF; f *= 2) {
                t += Math.abs(improvedNoise(p_x, p_y, p_z)) / f;

                p_x *= 2;
                p_y *= 2;
                p_z *= 2;
            }

            return t - 0.3;
        }

        /**
         * Create a turbulance function in 2D using the original perlin noise
         * function.
         *
         * @param x    The X coordinate of the location to sample
         * @param y    The Y coordinate of the location to sample
         * @param freq The frequency of the turbluance to create
         * @return The value at the given coordinates
         */
        private float turbulence2(float x, float y, float freq) {
            float t = 0;

            do {
                t += noise(freq * x, freq * y) / freq;
                freq *= 0.5f;
            }
            while (freq >= 1);

            return t;
        }

        /**
         * Create a turbulance function in 3D using the original perlin noise
         * function.
         *
         * @param x    The X coordinate of the location to sample
         * @param y    The Y coordinate of the location to sample
         * @param z    The Z coordinate of the location to sample
         * @param freq The frequency of the turbluance to create
         * @return The value at the given coordinates
         */
        private float turbulence3(float x, float y, float z, float freq) {
            float t = 0;

            do {
                t += noise(freq * x, freq * y, freq * z) / freq;
                freq *= 0.5f;
            }
            while (freq >= 1);

            return t;
        }

        /**
         * Create a 1D tileable noise function for the given width.
         *
         * @param x The X coordinate to generate the noise for
         * @param w The width of the tiled block
         * @return The value of the noise at the given coordinate
         */
        private float tileableNoise1(float x, float w) {
            return (noise(x) * (w - x) +
                    noise(x - w) * x) / w;
        }

        /**
         * Create a 2D tileable noise function for the given width and height.
         *
         * @param x The X coordinate to generate the noise for
         * @param y The Y coordinate to generate the noise for
         * @param w The width of the tiled block
         * @param h The height of the tiled block
         * @return The value of the noise at the given coordinate
         */
        private float tileableNoise2(float x, float y, float w, float h) {
            return (noise(x, y) * (w - x) * (h - y) +
                    noise(x - w, y) * x * (h - y) +
                    noise(x, y - h) * (w - x) * y +
                    noise(x - w, y - h) * x * y) / (w * h);
        }

        /**
         * Create a 3D tileable noise function for the given width, height and
         * depth.
         *
         * @param x The X coordinate to generate the noise for
         * @param y The Y coordinate to generate the noise for
         * @param z The Z coordinate to generate the noise for
         * @param w The width of the tiled block
         * @param h The height of the tiled block
         * @param d The depth of the tiled block
         * @return The value of the noise at the given coordinate
         */
        private float tileableNoise3(float x, float y, float z, float w, float h, float d) {
            return (noise(x, y, z) * (w - x) * (h - y) * (d - z) +
                    noise(x - w, y, z) * x * (h - y) * (d - z) +
                    noise(x, y - h, z) * (w - x) * y * (d - z) +
                    noise(x - w, y - h, z) * x * y * (d - z) +
                    noise(x, y, z - d) * (w - x) * (h - y) * z +
                    noise(x - w, y, z - d) * x * (h - y) * z +
                    noise(x, y - h, z - d) * (w - x) * y * z +
                    noise(x - w, y - h, z - d) * x * y * z) /
                    (w * h * d);
        }

        /**
         * Create a turbulance function that can be tiled across a surface in 2D.
         *
         * @param x    The X coordinate of the location to sample
         * @param y    The Y coordinate of the location to sample
         * @param w    The width to tile over
         * @param h    The height to tile over
         * @param freq The frequency of the turbluance to create
         * @return The value at the given coordinates
         */
        private float tileableTurbulence2(float x, float y, float w, float h, float freq) {
            float t = 0;

            do {
                t += tileableNoise2(freq * x, freq * y, w * freq, h * freq) / freq;
                freq *= 0.5f;
            } while (freq >= 1);

            return t;
        }

        /**
         * Create a turbulance function that can be tiled across a surface in 3D.
         *
         * @param x    The X coordinate of the location to sample
         * @param y    The Y coordinate of the location to sample
         * @param z    The Z coordinate of the location to sample
         * @param w    The width to tile over
         * @param h    The height to tile over
         * @param d    The depth to tile over
         * @param freq The frequency of the turbluance to create
         * @return The value at the given coordinates
         */
        private float tileableTurbulence3(float x, float y, float z, float w, float h, float d, float freq) {
            float t = 0;

            do {
                t += tileableNoise3(freq * x,
                        freq * y,
                        freq * z,
                        w * freq,
                        h * freq,
                        d * freq) / freq;
                freq *= 0.5f;
            } while (freq >= 1);

            return t;
        }


        /**
         * Simple lerp function using doubles.
         */
        private double lerp(double t, double a, double b) {
            return a + t * (b - a);
        }

        /**
         * Simple lerp function using floats.
         */
        private float lerp(float t, float a, float b) {
            return a + t * (b - a);
        }

        /**
         * Fade curve calculation which is 6t^5 - 15t^4 + 10t^3. This is the new
         * algorithm, where the old one used to be 3t^2 - 2t^3.
         *
         * @param t The t parameter to calculate the fade for
         * @return the drop-off amount.
         */
        private double fade(double t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        /**
         * Calculate the gradient function based on the hash code.
         */
        private double grad(int hash, double x, double y, double z) {
            // Convert low 4 bits of hash code into 12 gradient directions.
            int h = hash & 15;
            double u = (h < 8 || h == 12 || h == 13) ? x : y;
            double v = (h < 4 || h == 12 || h == 13) ? y : z;

            return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
        }

        /**
         * Simple bias generator using exponents.
         */
        private float bias(float a, float b) {
            return (float) Math.pow(a, Math.log(b) / LOG_HALF);
        }


        /*
         * Gain generator that caps to the range of [0, 1].
         */
        private float gain(float a, float b) {
            if (a < 0.001f)
                return 0;
            else if (a > 0.999f)
                return 1.0f;

            double p = Math.log(1.0f - b) / LOG_HALF;

            if (a < 0.5f)
                return (float) (Math.pow(2 * a, p) / 2);
            else
                return 1 - (float) (Math.pow(2 * (1.0f - a), p) / 2);
        }

        /**
         * S-curve function for value distribution for Perlin-1 noise function.
         */
        private float sCurve(float t) {
            return (t * t * (3 - 2 * t));
        }

        /**
         * 2D-vector normalisation function.
         */
        private void normalize2(float[] v) {
            float s = (float) (1 / Math.sqrt(v[0] * v[0] + v[1] * v[1]));
            v[0] *= s;
            v[1] *= s;
        }

        /**
         * 3D-vector normalisation function.
         */
        private void normalize3(float[] v) {
            float s = (float) (1 / Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]));
            v[0] *= s;
            v[1] *= s;
            v[2] *= s;
        }

        /**
         * Initialise the lookup arrays used by Perlin 1 function.
         */
        private void initPerlin1() {
            p = new int[B + B + 2];
            g3 = new float[B + B + 2][3];
            g2 = new float[B + B + 2][2];
            g1 = new float[B + B + 2];
            int i, j, k;

            for (i = 0; i < B; i++) {
                p[i] = i;

                g1[i] = (float) (((Math.random() * Integer.MAX_VALUE) % (B + B)) - B) / B;

                for (j = 0; j < 2; j++)
                    g2[i][j] = (float) (((Math.random() * Integer.MAX_VALUE) % (B + B)) - B) / B;
                normalize2(g2[i]);

                for (j = 0; j < 3; j++)
                    g3[i][j] = (float) (((Math.random() * Integer.MAX_VALUE) % (B + B)) - B) / B;
                normalize3(g3[i]);
            }

            while (--i > 0) {
                k = p[i];
                j = (int) ((Math.random() * Integer.MAX_VALUE) % B);
                p[i] = p[j];
                p[j] = k;
            }

            for (i = 0; i < B + 2; i++) {
                p[B + i] = p[i];
                g1[B + i] = g1[i];
                for (j = 0; j < 2; j++)
                    g2[B + i][j] = g2[i][j];
                for (j = 0; j < 3; j++)
                    g3[B + i][j] = g3[i][j];
            }
        }
    }
}
