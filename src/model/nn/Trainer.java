package model.nn;

import model.nn.trainset.TrainSet;

/**
 * {@link Trainer}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Trainer {
    public static void train(NeuralNetwork nn, double[] input, double[] target, double eta) {
        if (input.length != nn.getInputSize() || target.length != nn.getOutputSize()) return;

        nn.feedForward(input);
        nn.backPropagationError(target);
        nn.updateWeights(eta);
    }

    public static void train(NeuralNetwork nn, TrainSet trainSet, int loops, int batch_size) {
        if (trainSet.getInputSize() != nn.getInputSize() || trainSet.getOutputSize() != nn.getOutputSize()) return;
        for (int i = 0; i < loops; i++) {
            TrainSet batch = trainSet.extractBatch(batch_size);
            for (int j = 0; j < batch_size; j++) {
                train(nn, batch.getInput(j), batch.getTarget(j), 0.3);
            }
        }
    }

    public static void train(NeuralNetwork nn, TrainSet trainSet, int loops) {
        if (trainSet.getInputSize() != nn.getInputSize() || trainSet.getOutputSize() != nn.getOutputSize()) return;
        for (int i = 0; i < loops; i++) {
            for (int j = 0; j < trainSet.size(); j++) {
                train(nn, trainSet.getInput(j), trainSet.getTarget(j), 0.3);
            }
        }
    }

    public static void train(NeuralNetwork nn, TrainSet trainSet) {
        if (trainSet.getInputSize() != nn.getInputSize() || trainSet.getOutputSize() != nn.getOutputSize()) return;
        for (int i = 0; i < trainSet.size(); i++) {
            train(nn, trainSet.getInput(i), trainSet.getTarget(i), 0.3);
        }
    }
}
