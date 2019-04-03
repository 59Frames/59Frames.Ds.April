package model.networks.datastructs;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import model.networks.NetworkModel;
import model.networks.loss.Loss;
import model.networks.unit.Nonlinearity;

public abstract class DataSet implements Serializable {
    protected int inputDimension;
    protected int outputDimension;
    protected Loss lossTraining;
    protected Loss lossReporting;
    protected List<DataSequence> training;
    protected List<DataSequence> validation;
    protected List<DataSequence> testing;

    public abstract void displayReport(NetworkModel model, Random rng) throws Exception;

    public abstract Nonlinearity getModelOutputUnitToUse();

    public int getInputDimension() {
        return this.inputDimension;
    }

    public int getOutputDimension() {
        return this.outputDimension;
    }

    public List<DataSequence> getTraining() {
        return this.training;
    }

    public List<DataSequence> getValidation() {
        return this.validation;
    }

    public List<DataSequence> getTesting() {
        return this.testing;
    }

    public Loss getLossTraining() {
        return this.lossTraining;
    }

    public Loss getLossReporting() {
        return this.lossReporting;
    }
}
