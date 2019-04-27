package module.bootstrap;

import environment.Environment;
import module.Module;
import module.sensorium.sense.hearing.buffer.CircularByteBufferHolder;
import module.speech.language.Dictionaries;
import module.speech.language.SpellCorrector;
import util.Debugger;

/**
 * {@link Bootstrap}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */

public final class Bootstrap extends Module {
    /**
     * Constructs a new instance of type Module
     */
    public Bootstrap() {
        super("Bootstrap");
    }

    private void loadEnvironment() {
        Environment.load();
    }

    @Override
    protected void bootUp() {
        loadEnvironment();
        loadArc();

        registerBuffers();
        registerVocabulary();
    }

    private void loadArc() {
        try {
            Class.forName("module.sensorium.physical.Arc");

            Debugger.info("Arc Registered");
        } catch (ClassNotFoundException e) {
            Debugger.exception(e);
        }
    }

    private void registerBuffers() {
        CircularByteBufferHolder.registerCircularBuffer(Environment.getInteger("sensorium.sense.hearing.buffer.capacity"));

        Debugger.info("Buffers Registered");
    }

    private void registerVocabulary() {
        SpellCorrector.load(Dictionaries.valueOf(Environment.get("speech.default.dictionary")));

        Debugger.info(String.format("Vocabulary { %s } Loaded", SpellCorrector.getCurrentDictionary().getName()));
    }
}
