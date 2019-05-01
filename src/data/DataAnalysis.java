package data;

import java.util.*;

/**
 * {@link DataAnalysis}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class DataAnalysis {

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

    public DataAnalysis(double... arr) {
        if (arr.length < 2)
            throw new IllegalArgumentException("Array must contain more than 2 Values");

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

        System.out.printf(format, "Eingegebene Liste", Arrays.toString(unsortedArray));
        System.out.printf(format, "Sortierte Liste", Arrays.toString(sortedArray));

        System.out.println();

        System.out.printf(format, "Anzahl Daten", sortedArray.length);
        System.out.printf(format, "Tiefster Wert", sortedArray[0]);
        System.out.printf(format, "Höchster Wert", sortedArray[sortedArray.length - 1]);

        System.out.println();

        System.out.printf(format, "Median", median);
        System.out.printf(format, "Modus [Wert]", modusEntry.getKey());
        System.out.printf(format, "Modus [Häufigkeit]", modusEntry.getValue());
        System.out.printf(format, "Mittelwert", average);
        System.out.printf(format, "Q1", q1);
        System.out.printf(format, "Q2", median);
        System.out.printf(format, "Q3", q3);
        System.out.printf(format, "IQR", iqr);
        System.out.printf(format, "Unterer Whisker", lowerWhisker);
        System.out.printf(format, "Oberer Whisker", upperWhisker);
        System.out.printf(format, "Spannweite", range);
        System.out.printf(format, "Standardabweichung", defaultDeviation);
    }

    private double calcLowerWhisker() {
        double result = q3 - 1.5 * iqr;

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

    private double getDefaultDeviation() {
        double result = 0d;

        for (double val : sortedArray) {
            result += Math.pow(val - average, 2);
        }

        return Math.sqrt(result / sortedArray.length - 1);
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