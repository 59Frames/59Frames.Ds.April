package management;

import module.bootstrap.Bootstrap;
import module.emotion.Emotion;
import module.knowledge.Knowledge;
import module.motorium.Motorium;
import module.sensorium.Sensorium;
import module.speech.Speech;
import module.volition.Volition;

/**
 * {@link ModuleManagement}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ModuleManagement {
    private static final Bootstrap bootstrap = new Bootstrap();
    private static final Emotion emotion = new Emotion();
    private static final Knowledge knowledge = new Knowledge();
    private static final Motorium motorium = new Motorium();
    private static final Sensorium sensorium = new Sensorium();
    private static final Speech speech = new Speech();
    private static final Volition volition = new Volition();

    public static synchronized void boot() {
        bootstrap.boot();
        speech.boot();
        volition.boot();
        knowledge.boot();
        emotion.boot();
        motorium.boot();
        sensorium.boot();
    }
}
