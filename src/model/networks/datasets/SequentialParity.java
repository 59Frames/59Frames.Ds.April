package model.networks.datasets;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.networks.Matrix;
import model.networks.NetworkModel;
import model.networks.datastructs.DataSequence;
import model.networks.datastructs.DataSet;
import model.networks.datastructs.DataStep;
import model.networks.loss.LossMultiDimensionalBinary;
import model.networks.loss.LossSumOfSquares;
import model.networks.unit.Nonlinearity;
import model.networks.unit.SigmoidUnit;


public class SequentialParity extends DataSet {

    public SequentialParity(Random r, int total_sequences, int max_sequence_length_train, int max_sequence_length_test) {
        this.inputDimension = 1;
        this.outputDimension = 1;
        this.lossTraining = new LossSumOfSquares();
        this.lossReporting = new LossMultiDimensionalBinary();
        this.training = generateSequences(r, total_sequences, max_sequence_length_train);

        //training.addAll(generateSequences(r, total_sequences, max_sequence_length_test));

        this.validation = generateSequences(r, total_sequences, max_sequence_length_train);
        this.testing = generateSequences(r, total_sequences, max_sequence_length_test);
    }

    private static List<DataSequence> generateSequences(Random r, int total_sequences, int max_sequence_length) {
        List<DataSequence> result = new ArrayList<>();
        for (int s = 0; s < total_sequences; s++) {
            DataSequence sequence = new DataSequence();
            int tot = 0;
            int tempSequenceLength = r.nextInt(max_sequence_length) + 1;
            for (int t = 0; t < tempSequenceLength; t++) {
                DataStep step = new DataStep();
                double[] input = {0.0};

                if (r.nextDouble() < 0.5) {
                    input[0] = 1.0;
                    tot++;
                }
                step.input = new Matrix(input);

                double[] targetOutput;
                if (t == tempSequenceLength - 1) {
                    targetOutput = new double[1];
                    targetOutput[0] = tot % 2;
                    step.targetOutput = new Matrix(targetOutput);
                }
                sequence.getSteps().add(step);
            }
            result.add(sequence);
        }
        return result;
    }

    @Override
    public void displayReport(NetworkModel model, Random rng) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public Nonlinearity getModelOutputUnitToUse() {
        return new SigmoidUnit();
    }
}
