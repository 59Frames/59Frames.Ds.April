package model.networks;

import java.io.Serializable;
import java.util.Random;


public class Matrix implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int rows;
    public final int cols;
    public final double[] w;
    public final double[] dw;
    public final double[] stepCache;

    public Matrix(int dim) {
        this.rows = dim;
        this.cols = 1;
        this.w = new double[rows * cols];
        this.dw = new double[rows * cols];
        this.stepCache = new double[rows * cols];
    }

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.w = new double[rows * cols];
        this.dw = new double[rows * cols];
        this.stepCache = new double[rows * cols];
    }

    public Matrix(double[] vector) {
        this.rows = vector.length;
        this.cols = 1;
        this.w = vector;
        this.dw = new double[vector.length];
        this.stepCache = new double[vector.length];
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result.append(String.format("%.4f", getW(r, c))).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public Matrix cloneMatrix() {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < w.length; i++) {
            result.w[i] = w[i];
            result.dw[i] = dw[i];
            result.stepCache[i] = stepCache[i];
        }
        return result;
    }

    public void resetDw() {
        for (int i = 0; i < dw.length; i++) {
            dw[i] = 0;
        }
    }

    public void resetStepCache() {
        for (int i = 0; i < stepCache.length; i++) {
            stepCache[i] = 0;
        }
    }

    public static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.cols, m.rows);
        for (int r = 0; r < m.rows; r++) {
            for (int c = 0; c < m.cols; c++) {
                result.setW(c, r, m.getW(r, c));
            }
        }
        return result;
    }

    public static Matrix rand(int rows, int cols, double initParamsStdDev, Random rng) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < result.w.length; i++) {
            result.w[i] = rng.nextGaussian() * initParamsStdDev;
        }
        return result;
    }

    public static Matrix ident(int dim) {
        Matrix result = new Matrix(dim, dim);
        for (int i = 0; i < dim; i++) {
            result.setW(i, i, 1.0);
        }
        return result;
    }

    public static Matrix uniform(int rows, int cols, double s) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < result.w.length; i++) {
            result.w[i] = s;
        }
        return result;
    }

    public static Matrix ones(int rows, int cols) {
        return uniform(rows, cols, 1.0);
    }

    public static Matrix negOnes(int rows, int cols) {
        return uniform(rows, cols, -1.0);
    }

    private int index(int row, int col) {
        return cols * row + col;
    }

    private double getW(int row, int col) {
        return w[index(row, col)];
    }

    private void setW(int row, int col, double val) {
        w[index(row, col)] = val;
    }
}
