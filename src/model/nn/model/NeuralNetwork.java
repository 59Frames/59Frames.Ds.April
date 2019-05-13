package model.nn.model;

import model.nn.autodiff.Graph;
import model.nn.matrix.Matrix;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork implements NNModel {

    private static final long serialVersionUID = 1L;
    List<NNModel> layers;

    public NeuralNetwork(List<NNModel> layers) {
        this.layers = layers;
    }

    @Override
    public Matrix forward(Matrix input, Graph g) throws Exception {
        Matrix prev = input;
        for (NNModel layer : layers) {
            prev = layer.forward(prev, g);
        }
        return prev;
    }

    @Override
    public void resetState() {
        for (NNModel layer : layers) {
            layer.resetState();
        }
    }

    @Override
    public List<Matrix> getParameters() {
        List<Matrix> result = new ArrayList<>();
        for (NNModel layer : layers) {
            result.addAll(layer.getParameters());
        }
        return result;
    }
}
