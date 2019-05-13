package model.nn.util;

import model.nn.matrix.Matrix;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Util {

    public static int pickIndexFromRandomVector(Matrix probs, Random r) throws Exception {
        double mass = 1.0;
        for (int i = 0; i < probs.w.length; i++) {
            double prob = probs.w[i] / mass;
            if (r.nextDouble() < prob) {
                return i;
            }
            mass -= probs.w[i];
        }
        throw new Exception("no target index selected");
    }
}
