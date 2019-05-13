package module.bootstrap;

import environment.Environment;
import model.interfaceable.Order;
import model.progress.ProgressManager;
import module.sensorium.physical.Arc;
import module.sensorium.sense.hearing.buffer.CircularByteBufferHolder;
import module.speech.language.Dictionaries;
import module.speech.language.SpellCorrector;
import org.jocl.CL;

import java.util.ArrayList;

/**
 * {@link Bootstrap}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */

public final class Bootstrap {

    private static boolean loaded = false;

    static {
        boot();
    }

    /**
     * Constructs a new instance of type Module
     */
    private Bootstrap() {
    }

    public static void boot() {
        if (loaded)
            return;

        ArrayList<Order> functions = new ArrayList<>();

        functions.add(Bootstrap::loadEnvironment);

        functions.add(Bootstrap::setupOpenCL);

        functions.add(Bootstrap::registerArc);
        functions.add(Bootstrap::registerBuffers);
        functions.add(Bootstrap::registerVocabulary);

        ProgressManager progressManager = new ProgressManager("Bootstrap", functions.size());

        progressManager.start();
        for (int i = 0; i < functions.size(); i++) {
            functions.get(i).execute();
            progressManager.update(i + 1);
            progressManager.printProgress();
        }
        System.out.println();

        loaded = true;
    }

    private static void setupOpenCL() {
        boolean value = Environment.getBoolean("cl.exceptions.enabled", true);
        CL.setExceptionsEnabled(value);
    }

    private static void loadEnvironment() {
        Environment.load();
    }

    private static void registerArc() {
        Arc.load();
    }

    private static void registerBuffers() {
        CircularByteBufferHolder.registerCircularBuffer(Environment.getInteger("sense.hearing.buffer.capacity"));
    }

    private static void registerVocabulary() {
        SpellCorrector.load(Dictionaries.valueOf(Environment.get("default.dictionary")));
    }
}
