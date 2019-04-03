package model.networks;

import java.io.Serializable;
import java.util.List;


public interface NetworkModel extends Serializable {
    Matrix forward(Matrix input, Graph g) throws Exception;

    void resetState();

    List<Matrix> getParameters();
}
