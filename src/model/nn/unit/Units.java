package model.nn.unit;

import model.nn.Nonlinearity;

/**
 * {@link Units}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Units {
    public static final Nonlinearity SIGMOID = new SigmoidUnit();
    public static final Nonlinearity LINEAR = new LinearUnit();
    public static final Nonlinearity RECTIFIED_LINEAR = new RectifiedLinearUnit();
    public static final Nonlinearity SINE = new SineUnit();
    public static final Nonlinearity TANH = new TanhUnit();
}
