package model.nn;

import util.CollectionUtil;

import java.io.Serializable;

/**
 * {@link NeuralNetwork}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class NeuralNetwork implements Serializable, Cloneable {

    private final int[] networkLayerSizes;
    private final int inputSize;
    private final int outputSize;
    private final int networkSize;

    private final Nonlinearity activationFunction;

    private double[][] output;
    private double[][][] weights;
    private double[][] bias;

    private double[][] error_signals;
    private double[][] output_derivative;


    public NeuralNetwork(Nonlinearity activationFunction, final int... networkLayerSizes) {
        this.activationFunction = activationFunction;
        this.networkLayerSizes = networkLayerSizes;
        this.inputSize = this.networkLayerSizes[0];
        this.networkSize = this.networkLayerSizes.length;
        this.outputSize = this.networkLayerSizes[networkSize - 1];

        this.output = new double[networkSize][];
        this.error_signals = new double[networkSize][];
        this.output_derivative = new double[networkSize][];
        this.weights = new double[networkSize][][];
        this.bias = new double[networkSize][];

        for (int i = 0; i < networkSize; i++) {
            this.output[i] = new double[this.networkLayerSizes[i]];
            this.error_signals[i] = new double[this.networkLayerSizes[i]];
            this.output_derivative[i] = new double[this.networkLayerSizes[i]];

            this.bias[i] = CollectionUtil.createRandomDoubleArray(this.networkLayerSizes[i], 0.3, 0.7);

            if (i > 0)
                weights[i] = CollectionUtil.createRandomDoubleMatrix(this.networkLayerSizes[i], this.networkLayerSizes[i - 1], -0.3, 0.5);
        }
    }

    public double[] feedForward(double... input) {
        if (input.length != this.inputSize)
            return null;

        this.output[0] = input;
        for (int layer = 1; layer < networkSize; layer++) {
            for (int neuron = 0; neuron < networkLayerSizes[layer]; neuron++) {
                double sum = bias[layer][neuron];
                for (int prevNeuron = 0; prevNeuron < networkLayerSizes[layer - 1]; prevNeuron++)
                    sum += output[layer - 1][prevNeuron] * weights[layer][neuron][prevNeuron];

                output[layer][neuron] = applyNonlinearity(sum);
                output_derivative[layer][neuron] = (output[layer][neuron] * (1 - output[layer][neuron]));
            }
        }

        return output[networkSize - 1];
    }

    public void backPropagationError(double[] target) {
        for (int neuron = 0; neuron < networkLayerSizes[networkSize - 1]; neuron++)
            error_signals[networkSize - 1][neuron] = (output[networkSize - 1][neuron] - target[neuron]) * output_derivative[networkSize - 1][neuron];

        for (int layer = networkSize - 2; layer > 0; layer--) {
            for (int neuron = 0; neuron < networkLayerSizes[layer]; neuron++) {
                double sum = 0;
                for (int nextNeuron = 0; nextNeuron < networkLayerSizes[layer + 1]; nextNeuron++)
                    sum += weights[layer + 1][nextNeuron][neuron] * error_signals[layer + 1][nextNeuron];

                this.error_signals[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
    }

    public void updateWeights(double eta) {
        for (int layer = 1; layer < networkSize; layer++) {
            for (int neuron = 0; neuron < networkLayerSizes[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < networkLayerSizes[layer - 1]; prevNeuron++) {
                    weights[layer][neuron][prevNeuron] += -eta * output[layer - 1][prevNeuron] * error_signals[layer][neuron];
                }

                bias[layer][neuron] += -eta * error_signals[layer][neuron];
            }
        }
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public int getNetworkSize() {
        return networkSize;
    }

    private double applyNonlinearity(double sum) {
        return activationFunction.forward(sum);
    }
}
