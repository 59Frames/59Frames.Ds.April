package model.networks.datastructs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DataSequence implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<DataStep> steps = new ArrayList<>();

    public DataSequence() {

    }

    public DataSequence(List<DataStep> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("========================================================\n");
        for (DataStep step : steps) {
            result.append(step.toString()).append("\n");
        }
        result.append("========================================================\n");
        return result.toString();
    }

    public List<DataStep> getSteps() {
        return this.steps;
    }
}
