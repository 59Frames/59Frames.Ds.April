package model.networks;

import java.util.ArrayList;
import java.util.List;

import model.networks.unit.Nonlinearity;


public class Graph {
    private boolean applyBackprop;

    private List<Runnable> backprop = new ArrayList<>();

    public Graph() {
        this.applyBackprop = true;
    }

    public Graph(boolean applyBackprop) {
        this.applyBackprop = applyBackprop;
    }

    public void backward() {
        for (int i = backprop.size() - 1; i >= 0; i--) {
            backprop.get(i).run();
        }
    }

    public Matrix concatVectors(final Matrix m1, final Matrix m2) throws Exception {
        if (m1.cols > 1 || m2.cols > 1) {
            throw new Exception("Expected column vectors");
        }
        final Matrix out = new Matrix(m1.rows + m2.rows);
        applyBackpropagation(m1, m2, out);
        if (this.applyBackprop) {
            Runnable bp = () -> applyBackpropagation(m1, m2, out);
            backprop.add(bp);
        }
        return out;
    }

    private void applyBackpropagation(final Matrix m1, final Matrix m2, final Matrix out) {
        getLoc1(m2, out, getLoc1(m1, out, 0));
    }

    private int getLoc1(Matrix m1, Matrix out, int loc1) {
        for (int i = 0; i < m1.w.length; i++) {
            m1.w[i] = out.w[loc1];
            m1.dw[i] = out.dw[loc1];
            m1.stepCache[i] = out.stepCache[loc1];
            loc1++;
        }
        return loc1;
    }

    public Matrix applyNonlinearity(final Nonlinearity neuron, final Matrix m) throws Exception {
        final Matrix out = new Matrix(m.rows, m.cols);
        final int n = m.w.length;
        for (int i = 0; i < n; i++) {
            out.w[i] = neuron.forward(m.w[i]);
        }
        if (this.applyBackprop) {
            Runnable bp = () -> {
                for (int i = 0; i < n; i++) {
                    m.dw[i] += neuron.backward(m.w[i]) * out.dw[i];
                }
            };
            backprop.add(bp);
        }
        return out;
    }

    public Matrix mul(final Matrix m1, final Matrix m2) throws Exception {
        if (m1.cols != m2.rows) {
            throw new Exception("matrix dimension mismatch");
        }

        final int m1rows = m1.rows;
        final int m1cols = m1.cols;
        final int m2cols = m2.cols;
        final Matrix out = new Matrix(m1rows, m2cols);
        for (int i = 0; i < m1rows; i++) {
            int m1col = m1cols * i;
            for (int j = 0; j < m2cols; j++) {
                double dot = 0;
                for (int k = 0; k < m1cols; k++) {
                    dot += m1.w[m1col + k] * m2.w[m2cols * k + j];
                }
                out.w[m2cols * i + j] = dot;
            }
        }
        if (this.applyBackprop) {
            Runnable bp = () -> {
                for (int i = 0; i < m1.rows; i++) {
                    int outcol = m2cols * i;
                    for (int j = 0; j < m2.cols; j++) {
                        double b = out.dw[outcol + j];
                        for (int k = 0; k < m1.cols; k++) {
                            m1.dw[m1cols * i + k] += m2.w[m2cols * k + j] * b;
                            m2.dw[m2cols * k + j] += m1.w[m1cols * i + k] * b;
                        }
                    }
                }
            };
            backprop.add(bp);
        }
        return out;
    }

    public Matrix add(final Matrix m1, final Matrix m2) throws Exception {
        if (m1.rows != m2.rows || m1.cols != m2.cols) {
            throw new Exception("matrix dimension mismatch");
        }
        final Matrix out = new Matrix(m1.rows, m1.cols);
        for (int i = 0; i < m1.w.length; i++) {
            out.w[i] = m1.w[i] + m2.w[i];
        }
        if (this.applyBackprop) {
            Runnable bp = new Runnable() {
                public void run() {
                    for (int i = 0; i < m1.w.length; i++) {
                        m1.dw[i] += out.dw[i];
                        m2.dw[i] += out.dw[i];
                    }
                }
            };
            backprop.add(bp);
        }
        return out;
    }

    public Matrix oneMinus(final Matrix m) throws Exception {
        return sub(Matrix.ones(m.rows, m.cols), m);
    }

    public Matrix sub(final Matrix m1, final Matrix m2) throws Exception {
        return add(m1, neg(m2));
    }

    public Matrix smul(final Matrix m, final double s) throws Exception {
        return elmul(m,  Matrix.uniform(m.rows, m.cols, s));
    }

    public Matrix smul(final double s, final Matrix m) throws Exception {
        return smul(m, s);
    }

    public Matrix neg(final Matrix m) throws Exception {
        return elmul(Matrix.negones(m.rows, m.cols), m);
    }

    public Matrix elmul(final Matrix m1, final Matrix m2) throws Exception {
        if (m1.rows != m2.rows || m1.cols != m2.cols) {
            throw new Exception("matrix dimension mismatch");
        }
        final Matrix out = new Matrix(m1.rows, m1.cols);
        for (int i = 0; i < m1.w.length; i++) {
            out.w[i] = m1.w[i] * m2.w[i];
        }
        if (this.applyBackprop) {
            Runnable bp = () -> {
                for (int i = 0; i < m1.w.length; i++) {
                    m1.dw[i] += m2.w[i] * out.dw[i];
                    m2.dw[i] += m1.w[i] * out.dw[i];
                }
            };
            backprop.add(bp);
        }
        return out;
    }
}
