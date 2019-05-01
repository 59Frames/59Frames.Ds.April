import data.DataAnalysis;

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

        var arr = new double[25];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = round(random(1, 50));
        }

        new DataAnalysis(arr).printAnalysis();
    }
}
