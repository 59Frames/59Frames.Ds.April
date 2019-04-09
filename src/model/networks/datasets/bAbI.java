package model.networks.datasets;


import model.networks.NetworkModel;
import model.networks.datastructs.DataSequence;
import model.networks.datastructs.DataSet;
import model.networks.datastructs.DataStep;
import model.networks.loss.LossArgMax;
import model.networks.loss.LossSoftmax;
import model.networks.unit.LinearUnit;
import model.networks.unit.Nonlinearity;
import util.FileUtil;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

import static util.FileUtil.load;


public class bAbI extends DataSet {

    public static void main(String[] args) throws Exception {
        System.out.println("testing...");

        Random rng = new Random();
        bAbI data = new bAbI(3, 100, true, rng);

        System.out.println("done.");
    }

    public bAbI(int setId, int totalExamples, boolean onlySupportingFacts, Random rng) throws Exception {
        final File folder = load("datasets/bAbI/en");
        List<String> fileNamesTrain = new ArrayList<>();
        List<String> fileNamesTest = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String path = fileEntry.getPath();
            if (path.contains("train")) {
                if (path.contains("qa" + setId + "_")) {
                    fileNamesTrain.add(path);
                }
            } else if (path.contains("test")) {
                if (path.contains("qa" + setId + "_")) {
                    fileNamesTest.add(path);
                }
            } else {
                throw new Exception("Unknown file type");
            }
        }

        List<Story> storiesTrain = getStories(fileNamesTrain, onlySupportingFacts);
        List<Story> storiesTest = getStories(fileNamesTest, onlySupportingFacts);

        while (storiesTrain.size() > totalExamples) {
            storiesTrain.remove(rng.nextInt(storiesTrain.size()));
        }
        while (storiesTest.size() > totalExamples) {
            storiesTest.remove(rng.nextInt(storiesTest.size()));
        }

        configureVocab(storiesTrain, storiesTest);

        training = getSequences(storiesTrain);
        testing = getSequences(storiesTest);
        validation = null;

        lossTraining = new LossSoftmax();
        lossReporting = new LossArgMax();

        inputDimension = training.get(0).getSteps().get(0).input.w.length;

        int loc = 0;
        while (training.get(0).getSteps().get(loc).targetOutput == null) {
            loc++;
        }
        outputDimension = training.get(0).getSteps().get(loc).targetOutput.w.length;
    }

    public static final String[] TASK_NAMES = {
            "Single Supporting Fact",
            "Two Supporting Facts",
            "Three Supporting Facts",
            "Two Arg. Relations",
            "Three Arg. Relations",
            "Yes/No Questions",
            "Counting",
            "Lists/Sets",
            "Simple Negation",
            "Indefinite Knowledge",
            "Basic Coreference",
            "Conjunction",
            "Compound Coreference",
            "Time Reasoning",
            "Basic Deduction",
            "Basic Induction",
            "Positional Reasoning",
            "Size Reasoning",
            "Path Finding",
            "Agent's Motivations"
    };

    class Statement {
        public Statement(String line) {
            String[] parts = line.split("\t");
            if (parts.length > 1) {
                String[] words = parts[0].replace("?", " ?").split(" ");
                lineNum = Integer.parseInt(words[0]);
                for (int i = 1; i < words.length; i++) {
                    question.add(words[i].toLowerCase());
                }

                answer = parts[1].toLowerCase();
                String[] facts = parts[2].split(" ");
                for (String s : facts) {
                    supportingFacts.add(Integer.parseInt(s));
                }
                isFact = false;
            } else {
                String[] words = line.replace(".", " .").split(" ");
                lineNum = Integer.parseInt(words[0]);
                for (int i = 1; i < words.length; i++) {
                    fact.add(words[i].toLowerCase());
                }
            }
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(lineNum + "");
            if (isFact) {
                for (String word : fact) {
                    result.append(" ").append(word);
                }
            } else {
                for (String word : question) {
                    result.append(" ").append(word);
                }
                result.append(" -> ").append(answer);
                for (Integer i : supportingFacts) {
                    result.append(" ").append(i);
                }
            }
            return result.toString();
        }

        boolean isFact = true;
        List<String> fact = new ArrayList<>();
        List<String> question = new ArrayList<>();
        String answer;
        List<Integer> supportingFacts = new ArrayList<>();
        int lineNum;
    }

    class Story {
        public Story(List<Statement> statements, boolean onlySupportingFacts) {

            if (onlySupportingFacts) {
                Set<Integer> supportingFactsAndQuestions = new HashSet<>();
                for (Statement statement : statements) {
                    if (!statement.isFact) {
                        supportingFactsAndQuestions.add(statement.lineNum);
                        supportingFactsAndQuestions.addAll(statement.supportingFacts);
                    }
                }
                List<Statement> trimmed = new ArrayList<>();
                for (Statement statement : statements) {
                    if (supportingFactsAndQuestions.contains(statement.lineNum)) {
                        trimmed.add(statement);
                    }
                }
                this.statements = trimmed;
            } else {
                this.statements = statements;
            }
        }

        List<Statement> statements;

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Statement statement : statements) {
                result.append(statement.toString()).append("\n");
            }
            return result.toString();
        }
    }

    List<Story> getStories(List<String> fileNames, boolean onlySupportingFacts) throws Exception {
        List<Statement> statements = new ArrayList<>();
        for (String fileName : fileNames) {
            File file = load(fileName);
            List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            for (String line : lines) {
                statements.add(new Statement(line));
            }
        }
        List<Story> stories = new ArrayList<>();
        int prevNum = 0;
        List<Statement> storyList = new ArrayList<>();
        boolean containsQuestion = false;
        int errors = 0;
        for (Statement statement : statements) {
            if (statement.lineNum < prevNum) {
                if (!containsQuestion) {
                    //System.out.println("Incomplete story");
                    //for (Statement st : storyList) {
                    //	System.out.println(st);
                    //}
                    errors++;
                } else {
                    Story story = new Story(storyList, onlySupportingFacts);
                    //System.out.println(story);
                    stories.add(story);
                }
                containsQuestion = false;
                storyList = new ArrayList<>();
            }
            if (!statement.isFact) {
                containsQuestion = true;
            }
            storyList.add(statement);
            prevNum = statement.lineNum;
        }
        Story story = new Story(storyList, onlySupportingFacts);
        //System.out.println(story);
        stories.add(story);
        if (errors > 0) {
            System.out.println("WARNING: " + errors + " INCORRECT STORIES REMOVED.");
        }
        return stories;
    }


    private List<String> inputVocab = new ArrayList<>();
    private List<String> outputVocab = new ArrayList<>();

    private void configureVocab(List<Story> storiesTrain, List<Story> storiesTest) {
        Set<String> inputVocabSet = new HashSet<>();
        Set<String> outputVocabSet = new HashSet<>();
        List<Story> allStories = new ArrayList<>();
        allStories.addAll(storiesTrain);
        allStories.addAll(storiesTest);
        for (Story story : allStories) {
            for (Statement statement : story.statements) {
                if (statement.isFact) {
                    inputVocabSet.addAll(statement.fact);
                } else {
                    inputVocabSet.addAll(statement.question);
                    outputVocabSet.add(statement.answer);
                }
            }
        }

        inputVocab.addAll(inputVocabSet);
        outputVocab.addAll(outputVocabSet);

        Collections.sort(inputVocab);
        Collections.sort(outputVocab);

        System.out.println("Possible answers: ");
        for (int i = 0; i < outputVocab.size(); i++) {
            System.out.println("\t[" + i + "]: " + outputVocab.get(i));
        }
    }

    private List<DataSequence> getSequences(List<Story> stories) {
        int inputDimension = inputVocab.size();
        int outputDimension = outputVocab.size();
        List<DataSequence> sequences = new ArrayList<>();
        ;
        for (Story story : stories) {

            List<DataStep> steps = new ArrayList<>();

            for (Statement statement : story.statements) {
                if (statement.isFact) {
                    for (int w = 0; w < statement.fact.size(); w++) {
                        double[] input = new double[inputDimension];
                        for (int i = 0; i < inputDimension; i++) {
                            if (statement.fact.get(w).equals(inputVocab.get(i))) {
                                input[i] = 1.0;
                                break;
                            }
                        }
                        steps.add(new DataStep(input, null));
                    }
                } else {
                    for (int w = 0; w < statement.question.size(); w++) {
                        double[] input = new double[inputDimension];
                        double[] targetOutput = null;
                        for (int i = 0; i < inputDimension; i++) {
                            if (statement.question.get(w).equals(inputVocab.get(i))) {
                                input[i] = 1.0;
                                break;
                            }
                        }
                        steps.add(new DataStep(input, targetOutput));
                    }
                    double[] input = new double[inputDimension];
                    double[] targetOutput = new double[outputDimension];
                    for (int i = 0; i < outputDimension; i++) {
                        if (statement.answer.equals(outputVocab.get(i))) {
                            targetOutput[i] = 1.0;
                            break;
                        }
                    }
                    steps.add(new DataStep(input, targetOutput));
                }
            }
            sequences.add(new DataSequence(steps));
        }
        return sequences;
    }

    @Override
    public void displayReport(NetworkModel model, Random rng) throws Exception {

    }

    @Override
    public Nonlinearity getModelOutputUnitToUse() {
        return new LinearUnit();
    }
}
