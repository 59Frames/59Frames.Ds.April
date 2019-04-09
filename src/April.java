import management.ModuleManagement;
import model.nn.NeuralNetwork;
import model.nn.Trainer;
import model.nn.trainset.TrainSet;
import model.nn.unit.*;

import java.util.Arrays;

public class April {

    public static void main(String[] args) throws Exception {
        var nn = new NeuralNetwork(Units.TANH, 128, 64, 52, 32, 16, 5, 1);
        var set = TrainSet.basedOn(nn);

        set.createSets(1000);

        Trainer.train(nn, set);

        System.out.println("Target: " + Arrays.toString(set.getTarget(0)));
        System.out.println("Actual: " + Arrays.toString(nn.feedForward(set.getInput(0))));
    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
