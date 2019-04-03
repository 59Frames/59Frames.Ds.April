package util;

import model.networks.NetworkModel;
import model.networks.NeuralNetwork;
import model.networks.layer.*;
import model.networks.unit.Nonlinearity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetworkHelper {

    public static NeuralNetwork makeLongShortTermMemoryNetwork(final int inputDimension, final int hiddenDimension, final int hiddenLayers, final int outputDimension, final Nonlinearity decoderUnit, final double initParamsStdDev, final Random rng) {
        List<NetworkModel> layers = new ArrayList<>();
        return getNeuralNetwork(inputDimension, hiddenDimension, hiddenLayers, outputDimension, decoderUnit, initParamsStdDev, rng, layers);
    }

    public static NeuralNetwork makeLongShortTermMemoryNetworkWithBottleneck(final int inputDimension, final int bottleneckDimension, final int hiddenDimension, final int hiddenLayers, final int outputDimension, final Nonlinearity decoderUnit, final double initParamsStdDev, final Random rng) {
        List<NetworkModel> layers = new ArrayList<>();
        layers.add(new LinearLayer(inputDimension, bottleneckDimension, initParamsStdDev, rng));
        return getNeuralNetwork(bottleneckDimension, hiddenDimension, hiddenLayers, outputDimension, decoderUnit, initParamsStdDev, rng, layers);
    }

    public static NeuralNetwork makeFeedForwardNetwork(final int inputDimension, final int hiddenDimension, final int hiddenLayers, final int outputDimension, final Nonlinearity hiddenUnit, final Nonlinearity decoderUnit, final double initParamsStdDev, final Random rng) {
        List<NetworkModel> layers = new ArrayList<>();
        if (hiddenLayers == 0) {
            layers.add(new FeedForwardLayer(inputDimension, outputDimension, decoderUnit, initParamsStdDev, rng));
            return new NeuralNetwork(layers);
        } else {
            for (int h = 0; h < hiddenLayers; h++) {
                if (h == 0)  layers.add(new FeedForwardLayer(inputDimension, hiddenDimension, hiddenUnit, initParamsStdDev, rng));
                else layers.add(new FeedForwardLayer(hiddenDimension, hiddenDimension, hiddenUnit, initParamsStdDev, rng));
            }
            layers.add(new FeedForwardLayer(hiddenDimension, outputDimension, decoderUnit, initParamsStdDev, rng));
            return new NeuralNetwork(layers);
        }
    }

    public static NeuralNetwork makeGatedRecurrentUnitNetwork(final int inputDimension, final int hiddenDimension, final int hiddenLayers, final int outputDimension, final Nonlinearity decoderUnit, final double initParamsStdDev, final Random rng) {
        List<NetworkModel> layers = new ArrayList<>();
        for (int h = 0; h < hiddenLayers; h++) {
            if (h == 0) layers.add(new GruLayer(inputDimension, hiddenDimension, initParamsStdDev, rng));
            else layers.add(new GruLayer(hiddenDimension, hiddenDimension, initParamsStdDev, rng));
        }
        layers.add(new FeedForwardLayer(hiddenDimension, outputDimension, decoderUnit, initParamsStdDev, rng));
        return new NeuralNetwork(layers);
    }

    public static NeuralNetwork makeDeepRecurrentNeuralNetwork(final int inputDimension, final int hiddenDimension, final int hiddenLayers, final int outputDimension, final Nonlinearity hiddenUnit, final Nonlinearity decoderUnit, final double initParamsStdDev, final Random rng) {
        List<NetworkModel> layers = new ArrayList<>();
        for (int h = 0; h < hiddenLayers; h++) {
            if (h == 0) layers.add(new RnnLayer(inputDimension, hiddenDimension, hiddenUnit, initParamsStdDev, rng));
            else layers.add(new RnnLayer(hiddenDimension, hiddenDimension, hiddenUnit, initParamsStdDev, rng));
        }
        layers.add(new FeedForwardLayer(hiddenDimension, outputDimension, decoderUnit, initParamsStdDev, rng));
        return new NeuralNetwork(layers);
    }

    @NotNull
    private static NeuralNetwork getNeuralNetwork(final int inputDimension, final int hiddenDimension, final int hiddenLayers, final int outputDimension, final Nonlinearity decoderUnit, final double initParamsStdDev, final Random rng, final List<NetworkModel> layers) {
        for (int h = 0; h < hiddenLayers; h++) {
            if (h == 0) layers.add(new LstmLayer(inputDimension, hiddenDimension, initParamsStdDev, rng));
            else layers.add(new LstmLayer(hiddenDimension, hiddenDimension, initParamsStdDev, rng));
        }
        layers.add(new FeedForwardLayer(hiddenDimension, outputDimension, decoderUnit, initParamsStdDev, rng));
        return new NeuralNetwork(layers);
    }
}
