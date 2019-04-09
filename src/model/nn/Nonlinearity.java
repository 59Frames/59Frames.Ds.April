package model.nn;

import java.io.Serializable;

/**
 * {@link Nonlinearity}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public interface Nonlinearity extends Serializable {
    double forward(double x);

    double backward(double x);
}
