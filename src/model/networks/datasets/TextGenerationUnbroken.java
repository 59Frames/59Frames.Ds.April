package model.networks.datasets;

import model.networks.Graph;
import model.networks.Matrix;
import model.networks.NetworkModel;
import model.networks.datastructs.DataSequence;
import model.networks.datastructs.DataSet;
import model.networks.datastructs.DataStep;
import model.networks.loss.LossSoftmax;
import model.networks.unit.LinearUnit;
import model.networks.unit.Nonlinearity;
import util.Debugger;
import util.MatrixUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class TextGenerationUnbroken extends DataSet {

    private static final long serialVersionUID = 1L;

    public static int REPORT_SEQUENCE_LENGTH = 100;
    public static boolean REPORT_PERPLEXITY = true;

    private static Map<String, Integer> charToIndex = new HashMap<>();
    private static Map<Integer, String> indexToChar = new HashMap<>();
    private static int dimension;

    public static String generateText(NetworkModel model, int steps, boolean argmax, double temperature, Random rng) throws Exception {
        Matrix start = new Matrix(dimension);
        model.resetState();
        Graph g = new Graph(false);
        Matrix input = start.clone();
        StringBuilder result = new StringBuilder();
        for (int s = 0; s < steps; s++) {
            Matrix logProbs = model.forward(input, g);
            Matrix probs = LossSoftmax.getSoftmaxProbs(logProbs, temperature);

            int indexChosen = pickRandomIndex(argmax, rng, probs);
            String ch = indexToChar.get(indexChosen);
            result.append(ch);
            for (int i = 0; i < input.w.length; i++) {
                input.w[i] = 0;
            }
            input.w[indexChosen] = 1.0;
        }
        result = new StringBuilder(result.toString().replace("\n", "\"\n\t\""));
        return result.toString();
    }

    static int pickRandomIndex(boolean argmax, Random rng, Matrix probs) throws Exception {
        int indexChosen = -1;
        if (argmax) {
            double high = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < probs.w.length; i++) {
                if (probs.w[i] > high) {
                    high = probs.w[i];
                    indexChosen = i;
                }
            }
        } else {
            indexChosen = MatrixUtil.pickIndexFromRandomVector(probs, rng);
        }
        return indexChosen;
    }

    public TextGenerationUnbroken(String path, int totalSequences, int sequenceMinLength, int sequenceMaxLength, Random rng) throws Exception {

        Debugger.info("Text generation task");
        Debugger.info("loading " + path + "...");

        File file = new File(path);
        List<String> lines_ = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        StringBuilder text = new StringBuilder();
        for (String line : lines_) {
            text.append(line).append("\n");
        }

        Set<String> chars = new HashSet<>();
        int id = 0;

        Debugger.info("Characters:");

        System.out.print("\t");

        for (int i = 0; i < text.length(); i++) {
            String ch = text.charAt(i) + "";
            if (!chars.contains(ch)) {
                if (ch.equals("\n")) {
                    System.out.print("\\n");
                } else {
                    System.out.print(ch);
                }
                chars.add(ch);
                charToIndex.put(ch, id);
                indexToChar.put(id, ch);
                id++;
            }
        }
        Debugger.info("");

        dimension = chars.size();

        List<DataSequence> sequences = new ArrayList<>();

        for (int s = 0; s < totalSequences; s++) {
            List<double[]> vecs = new ArrayList<>();
            int len = rng.nextInt(sequenceMaxLength - sequenceMinLength + 1) + sequenceMinLength;
            int start = rng.nextInt(text.length() - len);
            for (int i = 0; i < len; i++) {
                String ch = text.charAt(i + start) + "";
                int index = charToIndex.get(ch);
                double[] vec = new double[dimension];
                vec[index] = 1.0;
                vecs.add(vec);
            }
            DataSequence sequence = new DataSequence();
            for (int i = 0; i < vecs.size() - 1; i++) {
                sequence.getSteps().add(new DataStep(vecs.get(i), vecs.get(i + 1)));
            }
            sequences.add(sequence);
        }

        Debugger.info("Total unique chars = " + chars.size());

        training = sequences;
        lossTraining = new LossSoftmax();
        lossReporting = new LossSoftmax();
        inputDimension = sequences.get(0).getSteps().get(0).input.w.length;
        int loc = 0;
        while (sequences.get(0).getSteps().get(loc).targetOutput == null) {
            loc++;
        }
        outputDimension = sequences.get(0).getSteps().get(loc).targetOutput.w.length;
    }

    @Override
    public void displayReport(NetworkModel model, Random rng) throws Exception {
        Debugger.info("========================================");
        Debugger.info("Text Generation Unbroken Report:");
        if (REPORT_PERPLEXITY) {
            Debugger.info("\ncalculating perplexity over entire data set...");
            double perplexity = LossSoftmax.calculateMedianPerplexity(model, training);
            Debugger.info("\nMedian Perplexity = " + String.format("%.4f", perplexity));
        }
        double[] temperatures = {1, 0.75, 0.5, 0.25, 0.1};
        for (double temperature : temperatures) {
            Debugger.info("\nTemperature " + temperature + " prediction:");
            String guess = TextGenerationUnbroken.generateText(model, REPORT_SEQUENCE_LENGTH, false, temperature, rng);
            Debugger.info("\t\"..." + guess + "...\"");
        }
        Debugger.info("\nArgmax prediction:");
        String guess = TextGenerationUnbroken.generateText(model, REPORT_SEQUENCE_LENGTH, true, 1.0, rng);
        Debugger.info("\t\"..." + guess + "...\"");
        Debugger.info("========================================");
    }

    @Override
    public Nonlinearity getModelOutputUnitToUse() {
        return new LinearUnit();
    }
}
