import environment.Environment;
import model.concurrent.Promise;
import model.interfaceable.Processable;
import model.progress.ProgressManager;
import util.CollectionUtil;

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

        System.out.println(Environment.get("name"));
        System.out.println(Environment.get("version"));
        System.out.println(Environment.get("author"));

        System.out.println(Environment.get("util.thread.pool"));
    }
}
