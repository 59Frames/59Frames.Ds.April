package model.networks.unit;

import java.io.Serializable;


public interface Nonlinearity extends Serializable {
    double forward(double x);

    double backward(double x);
}
