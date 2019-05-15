package util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * {@link DataAnalysis}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataAnalysis {

    private final String exercise;
    private final double[] unsortedArray;
    private final double[] sortedArray;
    private final HashMap<Double, Integer> occurrences;
    private final Map.Entry<Double, Integer> modusEntry;
    private final double median;
    private final double average;
    private final double range;
    private final double defaultDeviation;
    private final double q1;
    private final double q3;
    private final double iqr;
    private final double upperWhisker;
    private final double lowerWhisker;

    public DataAnalysis(final double... arr) {
        this("", arr);
    }

    public DataAnalysis(@NotNull final String exercise, final double... arr) {
        if (arr.length < 2)
            throw new IllegalArgumentException("Array must contain more than 2 Values");

        this.exercise = exercise;
        this.unsortedArray = arr.clone();
        this.sortedArray = arr.clone();
        Arrays.sort(sortedArray);
        this.occurrences = countOccurrences();
        this.modusEntry = getModusEntry();
        this.median = calcMedian();
        this.average = calcAverage();
        this.range = getRange();
        this.defaultDeviation = getDefaultDeviation();
        this.q1 = calcQ1();
        this.q3 = calcQ3();
        this.iqr = q3 - q1;
        this.upperWhisker = calcUpperWhisker();
        this.lowerWhisker = calcLowerWhisker();
    }

    public void printAnalysis() {

        final String format = "%-24s%-15s\n";

        if (!exercise.isBlank() || !exercise.isEmpty()) {
            System.out.printf(format, "Exercise", exercise.toUpperCase());

            System.out.println();
        }

        System.out.printf(format, "Input", Arrays.toString(unsortedArray));
        System.out.printf(format, "Sorted", Arrays.toString(sortedArray));

        System.out.println();

        System.out.printf(format, "N", sortedArray.length);
        System.out.printf(format, "Lowest value", sortedArray[0]);
        System.out.printf(format, "Highest value", sortedArray[sortedArray.length - 1]);

        System.out.println();

        System.out.printf(format, "Median", median);
        System.out.printf(format, "Modus [Value]", modusEntry.getKey());
        System.out.printf(format, "Modus [Occurrence]", modusEntry.getValue());
        System.out.printf(format, "Average", average);
        System.out.printf(format, "Q1", q1);
        System.out.printf(format, "Q2", median);
        System.out.printf(format, "Q3", q3);
        System.out.printf(format, "IQR", iqr);
        System.out.printf(format, "Lower whisker", lowerWhisker);
        System.out.printf(format, "Upper Whisker", upperWhisker);
        System.out.printf(format, "Range", range);
        System.out.printf(format, "Default deviation", defaultDeviation);

        System.out.printf(format, "Misfits", calcMisfits());
    }

    private ArrayList<Double> calcMisfits() {
        final ArrayList<Double> list = new ArrayList<>();

        for (double n : sortedArray) {
            if (n < lowerWhisker || n > upperWhisker)
                list.add(n);
        }

        return list;
    }

    private double calcLowerWhisker() {
        double result = q1 - 1.5 * iqr;

        for (double v : sortedArray) {
            if (v > result)
                return v;
        }

        return 0d;
    }

    private double calcUpperWhisker() {
        double result = q3 + 1.5 * iqr;

        for (int i = sortedArray.length - 1; i >= 0; i--) {
            if (sortedArray[i] < result)
                return sortedArray[i];
        }

        return 0;
    }

    // Standardabweichung
    private double getDefaultDeviation() {
        double result = 0;

        for (double val : sortedArray)
            result += Math.pow(val - average, 2);

        return Math.sqrt(result / (sortedArray.length - 1));
    }

    private double getRange() {
        return sortedArray[sortedArray.length - 1] - sortedArray[0];
    }

    private double calcQ1() {
        double result = 0.25 * (sortedArray.length - 1) + 1;
        int index = (int) result;
        double weight = result - index;
        return sortedArray[index - 1] + (weight * (sortedArray[index] - sortedArray[index - 1]));
    }

    private double calcQ3() {
        double result = 0.75 * (sortedArray.length - 1) + 1;
        int index = (int) result;
        double weight = result - index;
        return sortedArray[index - 1] + (weight * (sortedArray[index] - sortedArray[index - 1]));
    }

    private double calcAverage() {
        double sum = 0.0;

        for (double n : sortedArray)
            sum += n;

        return (sum / sortedArray.length);
    }

    private Map.Entry<Double, Integer> getModusEntry() {
        Map.Entry<Double, Integer> maxEntry = null;

        for (Map.Entry<Double, Integer> entry : occurrences.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return maxEntry;
    }

    private double calcMedian() {
        int mid = sortedArray.length / 2;

        return sortedArray.length % 2 == 1
                ? sortedArray[mid]
                : (sortedArray[mid - 1] + sortedArray[mid]) / 2;
    }

    private HashMap<Double, Integer> countOccurrences() {
        final HashMap<Double, Integer> occurrences = new HashMap<>();
        for (double val : sortedArray) {
            if (occurrences.containsKey(val)) {
                occurrences.put(val, occurrences.get(val) + 1);
            } else {
                occurrences.put(val, 1);
            }
        }
        return occurrences;
    }
}
