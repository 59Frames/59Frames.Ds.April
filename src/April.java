import model.concurrent.Promise;
import model.interfaceable.Processable;
import model.progress.ProgressManager;

import java.util.concurrent.Callable;

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



        long total = 512;

        ProgressManager progressManager = new ProgressManager(total);
        progressManager.start();
        for (int i = 1; i <= total; i+=randomInt(1, 3)) {
            try {
                Thread.sleep(randomInt(10, 100));
                progressManager.update(i);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
