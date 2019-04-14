package util;

import model.networks.Matrix;

import java.util.Random;

/**
 * {@link MatrixUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class MatrixUtil {

    private MatrixUtil() {
    }

    public static int pickIndexFromRandomVector(Matrix matrix, Random r) throws Exception {
        double mass = 1.0;
        for (int i = 0; i < matrix.w.length; i++) {
            double prob = matrix.w[i] / mass;
            if (r.nextDouble() < prob) {
                return i;
            }
            mass -= matrix.w[i];
        }
        throw new Exception("no target index selected");
    }
}
