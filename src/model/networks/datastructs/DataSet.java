package model.networks.datastructs;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import model.networks.NetworkModel;
import model.networks.loss.Loss;
import model.networks.unit.Nonlinearity;

public abstract class DataSet implements Serializable {
    public int inputDimension;
    public int outputDimension;
    public Loss lossTraining;
    public Loss lossReporting;
    public List<DataSequence> training;
    public List<DataSequence> validation;
    public List<DataSequence> testing;

    public abstract void displayReport(NetworkModel model, Random rng) throws Exception;

    public abstract Nonlinearity getModelOutputUnitToUse();
}
