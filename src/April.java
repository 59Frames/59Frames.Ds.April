import management.ModuleManagement;
import model.networks.NeuralNetwork;
import model.networks.Trainer;
import model.networks.datasets.SequentialParity;
import model.networks.datasets.TextGeneration;
import model.networks.datasets.bAbI;
import model.networks.datastructs.DataSet;
import model.networks.unit.LinearUnit;
import model.networks.unit.SigmoidUnit;
import util.CastUtil;
import util.NeuralNetworkHelper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class April {

    public static void main(String[] args) throws Exception {

    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
