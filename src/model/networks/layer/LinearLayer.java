package model.networks.layer;

import model.networks.Graph;
import model.networks.Matrix;
import model.networks.NetworkModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LinearLayer implements NetworkModel {

    private static final long serialVersionUID = 1L;
    private Matrix W;

    public LinearLayer(int inputDimension, int outputDimension, double initParamsStdDev, Random rng) {
        W = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
    }

    @Override
    public Matrix forward(Matrix input, Graph g) throws Exception {
        return g.mul(W, input);
    }

    @Override
    public void resetState() {

    }

    @Override
    public List<Matrix> getParameters() {
        List<Matrix> result = new ArrayList<>();
        result.add(W);
        return result;
    }
}
