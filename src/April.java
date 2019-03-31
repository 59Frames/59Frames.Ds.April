import module.Module;
import module.bootstrap.Bootstrap;
import module.emotion.Emotion;
import module.knowledge.Knowledge;
import module.motorium.Motorium;
import module.sensorium.Sensorium;
import module.speech.Speech;
import module.volition.Volition;
import org.jetbrains.annotations.NotNull;
import util.CommandUtil;

import java.util.LinkedHashMap;

public class April {

    private static final LinkedHashMap<String, Module> modules = new LinkedHashMap<>();

    public static void main(String[] args) {
        registerNativeModules();

        modules.forEach((key, module) -> module.boot());

        CommandUtil.start();
    }

    private static void registerNativeModules() {
        registerModule(new Bootstrap());
        registerModule(new Emotion());
        registerModule(new Knowledge());
        registerModule(new Motorium());
        registerModule(new Sensorium());
        registerModule(new Speech());
        registerModule(new Volition());
    }

    private static void registerModule(@NotNull final Module module) {
        modules.put(module.getModuleName(), module);
    }
}
