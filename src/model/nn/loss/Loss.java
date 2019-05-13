package model.nn.loss;


import model.nn.matrix.Matrix;

import java.io.Serializable;

public interface Loss extends Serializable {
    void backward(Matrix actualOutput, Matrix targetOutput) throws Exception;

    double measure(Matrix actualOutput, Matrix targetOutput) throws Exception;
}
