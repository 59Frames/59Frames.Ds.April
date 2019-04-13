package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class Silvester {

    @Contract(pure = true)
    private Silvester() {
    }

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

    @Contract(pure = true)
    public static int round(float n) {
        return Math.round(n);
    }

    @Contract(pure = true)
    public static long round(double n) {
        return Math.round(n);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float round(float value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Contract(pure = true)
    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    @Contract(pure = true)
    public static <T extends Comparable<T>> T max(@NotNull final T a, @NotNull final T b) {
        return (a.compareTo(b) >= 0) ? a : b;
    }

    @Contract(pure = true)
    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static float min(float a, float b) {
        return Math.min(a, b);
    }

    @Contract(pure = true)
    public static <T extends Comparable<T>> T min(@NotNull final T a, @NotNull final T b) {
        return (a.compareTo(b) <= 0) ? a : b;
    }

    @Contract(pure = true)
    public static double copySign(double magnitude, double sign) {
        return Math.copySign(magnitude, sign);
    }

    @Contract(pure = true)
    public static float copySign(float magnitude, float sign) {
        return Math.copySign(magnitude, sign);
    }

    public static double pow(double a, double b) {
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

    @Contract(pure = true)
    public static double average(@NotNull final float[] arr) {
        double sum = 0.0;

        for (float n : arr)
            sum += n;

        return (sum / arr.length);
    }

    public static double median(@NotNull List<? extends Number> values) {
        values = values.parallelStream().sorted((Comparator<Number>) (o1, o2) -> {
            Double d1 = (o1 == null) ? Double.POSITIVE_INFINITY : o1.doubleValue();
            Double d2 = (o2 == null) ? Double.POSITIVE_INFINITY : o2.doubleValue();
            return  d1.compareTo(d2);
        }).collect(Collectors.toList());

        int mid = values.size()/2;
        return values.size() % 2 == 1
                ? values.get(mid).doubleValue()
                : (values.get(mid-1).doubleValue() + values.get(mid).doubleValue()) / 2;
    }

    public static double random() {
        return Silvester.RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
    }

    public static double random(double bound) {
        return random() * bound;
    }

    public static double random(double a, double b) {
        final double max = max(a, b);
        final double min = min(a, b);
        return random() * (max - min) + min;
    }

    public static int randomInt() {
        return round((float) random(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    public static int randomInt(int bound) {
        return round((float) random(bound));
    }

    public static int randomInt(double a, double b) {
        return round((float) random(a, b));
    }

    public static int noiseInt(double prev) {
        return randomInt(prev - 3, prev + 3);
    }

    public static double noise(double prev) {
        return random(prev - 3, prev + 3);
    }

    @Contract(pure = true)
    public static double exp(double d) {
        return StrictMath.exp(d);
    }


    //Neural Network
    @Contract(pure = true)
    public static double signum(double d) {
        return StrictMath.signum(d);
    }

    @Contract(pure = true)
    public static float signum(float f) {
        return StrictMath.signum(f);
    }

    @Contract(pure = true)
    public static double sigmoid(double d) {
        return 1d / (1 + Math.exp(-d));
    }

    public static double hyperbolicTangent(double d) {
        return StrictMath.tanh(d);
    }

    public static double tanh(double d) {
        return hyperbolicTangent(d);
    }

    @Contract(pure = true)
    public static double rectify(double d) {
        return max(0, d);
    }


    // Geometric
    @Contract(pure = true)
    public static double volume(double width, double length, double height) {
        return width * length * height;
    }

    @Contract(pure = true)
    public static double surface(double width, double length) {
        return width * length;
    }

    public static double cylinderSurface(double radius, double height) {
        return (circular(radius) * 2) + (circumference(radius) * height);
    }

    public static double cylinderVolume(double radius, double height) {
        return circular(radius) * height;
    }

    @Contract(pure = true)
    public static double circumference(double radius) {
        return (radius * 2) * PI;
    }

    public static double circular(double radius) {
        return (pow(radius, 2)) * PI;
    }

    public static double rectangleSurface(double width, double length, double height) {
        return ((surface(width, length) * 2) + (surface(width, height) * 2) + (surface(length, height) * 2));
    }

    @Contract(pure = true)
    public static double rectangleVolume(double width, double length, double height) {
        return volume(width, length, height);
    }


    // Velocities
    @Contract(pure = true)
    public static double kmh2mph(double kmh) {
        return (kmh / 1.609);
    }

    @Contract(pure = true)
    public static double mph2kmh(double mph) {
        return (mph * 1.609);
    }

    @Contract(pure = true)
    public static double mph2knots(double mph) {
        return (mph / 1.151);
    }

    @Contract(pure = true)
    public static double knots2mph(double knots) {
        return (knots * 1.151);
    }

    @Contract(pure = true)
    public static double kmh2knots(double kmh) {
        return (kmh / 1.852);
    }

    @Contract(pure = true)
    public static double knots2kmh(double knots) {
        return (knots * 1.852);
    }

    @Contract(pure = true)
    public static double kmh2mps(double kmh) {
        return (kmh / 3.6);
    }

    @Contract(pure = true)
    public static double mps2kmh(double mps) {
        return (mps * 3.6);
    }

    @Contract(pure = true)
    public static double mph2mps(double mph) {
        return (mph / 2.237);
    }

    @Contract(pure = true)
    public static double mps2mph(double mps) {
        return (mps * 2.237);
    }


    // Temperatures
    @Contract(pure = true)
    public static double celsius2fahrenheit(double celsius) {
        return ((celsius * 9 / 5) + 32);
    }

    @Contract(pure = true)
    public static double celsius2kelvin(double celsius) {
        return (celsius + 273.15);
    }

    public static double fahrenheit2kelvin(double fahrenheit) {
        return celsius2kelvin(fahrenheit2celsius(fahrenheit));
    }

    @Contract(pure = true)
    public static double fahrenheit2celsius(double fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    @Contract(pure = true)
    public static double kelvin2celsius(double kelvin) {
        return (kelvin - 273.15);
    }

    public static double kelvin2fahrenheit(double kelvin) {
        return celsius2fahrenheit(kelvin2celsius(kelvin));
    }

    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = ThreadLocalRandom.current();

        @Contract(pure = true)
        private RandomNumberGeneratorHolder() {
        }
    }
}
