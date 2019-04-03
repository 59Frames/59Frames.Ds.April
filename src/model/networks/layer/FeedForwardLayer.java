package model.networks.layer;

import model.networks.Graph;
import model.networks.Matrix;
import model.networks.NetworkModel;
import model.networks.unit.Nonlinearity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FeedForwardLayer implements NetworkModel {

    private static final long serialVersionUID = 1L;
    private Matrix W;
    private Matrix b;
    private Nonlinearity f;

    public FeedForwardLayer(int inputDimension, int outputDimension, Nonlinearity f, double initParamsStdDev, Random rng) {
        W = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        b = new Matrix(outputDimension);
        this.f = f;
    }

    @Override
    public Matrix forward(@NotNull Matrix input, @NotNull Graph g) throws Exception {
        Matrix sum = g.add(g.mul(W, input), b);
        return g.nonlin(f, sum);
    }

    @Override
    public void resetState() {

    }

    @Override
    public List<Matrix> getParameters() {
        List<Matrix> result = new ArrayList<>();
        result.add(W);
        result.add(b);
        return result;
    }
}
