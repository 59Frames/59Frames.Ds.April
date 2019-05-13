package model.nn.model;

import model.nn.autodiff.Graph;
import model.nn.matrix.Matrix;

import java.io.Serializable;
import java.util.List;

public interface NNModel extends Serializable {
    Matrix forward(Matrix input, Graph g) throws Exception;

    void resetState();

    List<Matrix> getParameters();
}
