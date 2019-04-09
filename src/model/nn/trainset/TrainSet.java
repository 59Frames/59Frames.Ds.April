package model.nn.trainset;

import model.nn.NeuralNetwork;
import util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@link TrainSet}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class TrainSet {
    private final int inputSize;
    private final int outputSize;

    //double[][] <- index1: 0 = input, 1 = output || index2: index of element
    private ArrayList<double[][]> data = new ArrayList<>();

    public TrainSet(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public static TrainSet basedOn(NeuralNetwork nn) {
        return new TrainSet(nn.getInputSize(), nn.getOutputSize());
    }

    public void addData(double[] input, double[] target) {
        if (input.length != inputSize || target.length != outputSize) return;
        data.add(new double[][]{input, target});
    }

    public TrainSet extractBatch(int size) {
        if (size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(inputSize, outputSize);
            int[] ids = CollectionUtil.createRandomIntegerArray(size, 0, this.size() - 1);
            assert ids != null;
            for (Integer i : ids) {
                set.addData(this.getInput(i), this.getTarget(i));
            }
            return set;
        } else return this;
    }

    public void createSets(int loops) {
        for (int i = 0; i < loops; i++) {
            double[] input = new double[inputSize];
            double[] output = new double[outputSize];
            for (int k = 0; k < inputSize; k++) {
                input[k] = (double) ((int) (Math.random() * 10)) / (double) 10;
                if (k < outputSize) {
                    output[k] = (double) ((int) (Math.random() * 10)) / (double) 10;
                }
            }
            this.addData(input, output);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder("TrainSet [" + inputSize + " ; " + outputSize + "]\n");
        int index = 0;
        for (double[][] r : data) {
            s.append(index).append(":   ").append(Arrays.toString(r[0])).append("  >-||-<  ").append(Arrays.toString(r[1])).append("\n");
            index++;
        }
        return s.toString();
    }

    public int size() {
        return data.size();
    }

    public double[] getInput(int index) {
        if (index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getTarget(int index) {
        if (index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public int getInputSize() {
        return inputSize;
    }
}
