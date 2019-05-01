import data.DataAnalysis;
import util.CollectionUtil;

import static util.Toolbox.*;

public class April {
    public static void main(String[] args) throws Exception {
        // TODO: 28/04/2019 bootstrap
        // TODO: 28/04/2019 sensorium
        // TODO: 28/04/2019 speech
        // TODO: 28/04/2019 volition
        // TODO: 28/04/2019 knowledge
        // TODO: 28/04/2019 emotion
        // TODO: 28/04/2019 motorium

        new DataAnalysis(CollectionUtil.createRandomDoubleArray(50, 1, 10, 1)).printAnalysis();
    }
}
