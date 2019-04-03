package model.networks;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork implements NetworkModel {

    private static final long serialVersionUID = 1L;

    private List<NetworkModel> layers;

    public NeuralNetwork(List<NetworkModel> layers) {
        this.layers = layers;
    }

    @Override
    public Matrix forward(@NotNull Matrix input, @NotNull Graph g) throws Exception {
        Matrix prev = input;
        for (NetworkModel layer : layers) {
            prev = layer.forward(prev, g);
        }
        return prev;
    }

    @Override
    public void resetState() {
        for (NetworkModel layer : layers) {
            layer.resetState();
        }
    }

    @Override
    public List<Matrix> getParameters() {
        List<Matrix> result = new ArrayList<>();
        for (NetworkModel layer : layers) {
            result.addAll(layer.getParameters());
        }
        return result;
    }
}
