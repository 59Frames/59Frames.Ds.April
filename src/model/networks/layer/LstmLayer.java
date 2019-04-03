package model.networks.layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.networks.Graph;
import model.networks.Matrix;
import model.networks.NetworkModel;
import model.networks.unit.Nonlinearity;
import model.networks.unit.SigmoidUnit;
import model.networks.unit.TanhUnit;
import org.jetbrains.annotations.NotNull;

public class LstmLayer implements NetworkModel {

    private static final long serialVersionUID = 1L;

    private final int inputDimension;
    private final int outputDimension;

    private Matrix Wix, Wih, bi;
    private Matrix Wfx, Wfh, bf;
    private Matrix Wox, Woh, bo;
    private Matrix Wcx, Wch, bc;

    private Matrix hiddenContext;
    private Matrix cellContext;

    private Nonlinearity fInputGate = new SigmoidUnit();
    private Nonlinearity fForgetGate = new SigmoidUnit();
    private Nonlinearity fOutputGate = new SigmoidUnit();
    private Nonlinearity fCellInput = new TanhUnit();
    private Nonlinearity fCellOutput = new TanhUnit();

    public LstmLayer(int inputDimension, int outputDimension, double initParamsStdDev, Random rng) {
        this.inputDimension = inputDimension;
        this.outputDimension = outputDimension;
        Wix = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        Wih = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        bi = new Matrix(outputDimension);
        Wfx = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        Wfh = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        bf = Matrix.ones(outputDimension, 1);
        Wox = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        Woh = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        bo = new Matrix(outputDimension);
        Wcx = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        Wch = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        bc = new Matrix(outputDimension);
    }

    @Override
    public Matrix forward(@NotNull Matrix input, @NotNull Graph g) throws Exception {

        //input gate
        Matrix sum0 = g.mul(Wix, input);
        Matrix sum1 = g.mul(Wih, hiddenContext);
        Matrix inputGate = g.nonlin(fInputGate, g.add(g.add(sum0, sum1), bi));

        //forget gate
        Matrix sum2 = g.mul(Wfx, input);
        Matrix sum3 = g.mul(Wfh, hiddenContext);
        Matrix forgetGate = g.nonlin(fForgetGate, g.add(g.add(sum2, sum3), bf));

        //output gate
        Matrix sum4 = g.mul(Wox, input);
        Matrix sum5 = g.mul(Woh, hiddenContext);
        Matrix outputGate = g.nonlin(fOutputGate, g.add(g.add(sum4, sum5), bo));

        //write operation on cells
        Matrix sum6 = g.mul(Wcx, input);
        Matrix sum7 = g.mul(Wch, hiddenContext);
        Matrix cellInput = g.nonlin(fCellInput, g.add(g.add(sum6, sum7), bc));

        //compute new cell activation
        Matrix retainCell = g.elmul(forgetGate, cellContext);
        Matrix writeCell = g.elmul(inputGate, cellInput);
        Matrix cellAct = g.add(retainCell, writeCell);

        //compute hidden state as gated, saturated cell activations
        Matrix output = g.elmul(outputGate, g.nonlin(fCellOutput, cellAct));

        //rollover activations for next iteration
        hiddenContext = output;
        cellContext = cellAct;

        return output;
    }

    @Override
    public void resetState() {
        hiddenContext = new Matrix(outputDimension);
        cellContext = new Matrix(outputDimension);
    }

    @Override
    public List<Matrix> getParameters() {
        List<Matrix> result = addUpParams(Wix, Wih, bi, Wfx, Wfh, bf, Wox, Woh, bo);
        result.add(Wcx);
        result.add(Wch);
        result.add(bc);
        return result;
    }

    static List<Matrix> addUpParams(Matrix wix, Matrix wih, Matrix bi, Matrix wfx, Matrix wfh, Matrix bf, Matrix wox, Matrix woh, Matrix bo) {
        List<Matrix> result = new ArrayList<>();
        result.add(wix);
        result.add(wih);
        result.add(bi);
        result.add(wfx);
        result.add(wfh);
        result.add(bf);
        result.add(wox);
        result.add(woh);
        result.add(bo);
        return result;
    }
}
