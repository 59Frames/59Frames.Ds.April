package model.networks.layer;

import model.networks.Graph;
import model.networks.Matrix;
import model.networks.NetworkModel;
import model.networks.unit.Nonlinearity;
import model.networks.unit.SigmoidUnit;
import model.networks.unit.TanhUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class GruLayer implements NetworkModel {

    private static final long serialVersionUID = 1L;

    private final int inputDimension;
    private final int outputDimension;

    private Matrix IHmix, HHmix, Bmix;
    private Matrix IHnew, HHnew, Bnew;
    private Matrix IHreset, HHreset, Breset;

    private Matrix context;

    private Nonlinearity fMix = new SigmoidUnit();
    private Nonlinearity fReset = new SigmoidUnit();
    private Nonlinearity fNew = new TanhUnit();

    public GruLayer(int inputDimension, int outputDimension, double initParamsStdDev, Random rng) {
        this.inputDimension = inputDimension;
        this.outputDimension = outputDimension;
        IHmix = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        HHmix = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        Bmix = new Matrix(outputDimension);
        IHnew = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        HHnew = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        Bnew = new Matrix(outputDimension);
        IHreset = Matrix.rand(outputDimension, inputDimension, initParamsStdDev, rng);
        HHreset = Matrix.rand(outputDimension, outputDimension, initParamsStdDev, rng);
        Breset = new Matrix(outputDimension);
    }

    @Override
    public Matrix forward(@NotNull Matrix input, @NotNull Graph g) throws Exception {

        Matrix sum0 = g.mul(IHmix, input);
        Matrix sum1 = g.mul(HHmix, context);
        Matrix actMix = g.applyNonlinearity(fMix, g.add(g.add(sum0, sum1), Bmix));

        Matrix sum2 = g.mul(IHreset, input);
        Matrix sum3 = g.mul(HHreset, context);
        Matrix actReset = g.applyNonlinearity(fReset, g.add(g.add(sum2, sum3), Breset));

        Matrix sum4 = g.mul(IHnew, input);
        Matrix gatedContext = g.elmul(actReset, context);
        Matrix sum5 = g.mul(HHnew, gatedContext);
        Matrix actNewPlusGatedContext = g.applyNonlinearity(fNew, g.add(g.add(sum4, sum5), Bnew));

        Matrix memvals = g.elmul(actMix, context);
        Matrix newvals = g.elmul(g.oneMinus(actMix), actNewPlusGatedContext);
        Matrix output = g.add(memvals, newvals);

        //rollover activations for next iteration
        context = output;

        return output;
    }

    @Override
    public void resetState() {
        context = new Matrix(outputDimension);
    }

    @Override
    public List<Matrix> getParameters() {
        return LstmLayer.addUpParams(IHmix, HHmix, Bmix, IHnew, HHnew, Bnew, IHreset, HHreset, Breset);
    }

}
