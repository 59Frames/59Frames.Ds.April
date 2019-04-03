package model.networks.datastructs;

import java.io.Serializable;

import model.networks.Matrix;


public class DataStep implements Serializable {

    private static final long serialVersionUID = 1L;
    public Matrix input = null;
    public Matrix targetOutput = null;

    public DataStep() {

    }

    public DataStep(double[] input, double[] targetOutput) {
        this.input = new Matrix(input);
        if (targetOutput != null) {
            this.targetOutput = new Matrix(targetOutput);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.w.length; i++) {
            result.append(String.format("%.5f", input.w[i])).append("\t");
        }
        result.append("\t->\t");
        if (targetOutput != null) {
            for (int i = 0; i < targetOutput.w.length; i++) {
                result.append(String.format("%.5f", targetOutput.w[i])).append("\t");
            }
        } else {
            result.append("___\t");
        }
        return result.toString();
    }
}
