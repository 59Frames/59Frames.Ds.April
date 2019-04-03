import management.ModuleManagement;
import model.networks.NeuralNetwork;
import model.networks.Trainer;
import model.networks.datasets.TextGeneration;
import model.networks.datastructs.DataSet;
import util.NeuralNetworkHelper;

import java.util.Random;

public class April {

    public static void main(String[] args) throws Exception {

        DataSet data = new TextGeneration("datasets/text/PaulGraham.txt");
        String savePath = "networks/PaulGraham.ser";
        boolean initFromSaved = false; //set this to false to start with a fresh model
        boolean overwriteSaved = true;

        TextGeneration.REPORT_SEQUENCE_LENGTH = 100;
        TextGeneration.SINGLE_WORD_AUTO_CORRECT = false; //set this to true to constrain generated sentences to contain only words observed in the training data.

        int bottleneckSize = 10; //one-hot input is squeezed through this
        int hiddenDimension = 200;
        int hiddenLayers = 1;
        double learningRate = 0.001;
        double initParamsStdDev = 0.08;

        Random rng = new Random();
        NeuralNetwork network = NeuralNetworkHelper.makeLongShortTermMemoryNetworkWithBottleneck(
                data.inputDimension,
                bottleneckSize,
                hiddenDimension,
                hiddenLayers,
                data.outputDimension,
                data.getModelOutputUnitToUse(),
                initParamsStdDev, rng);

        int reportEveryNthEpoch = 10;
        int trainingEpochs = 10;

        Trainer.train(trainingEpochs, learningRate, network, data, reportEveryNthEpoch, initFromSaved, overwriteSaved, savePath, rng);

        System.out.println("done.");
    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
