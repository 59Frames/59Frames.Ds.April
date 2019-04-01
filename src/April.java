import management.ModuleManagement;
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

    public static void main(String[] args) {
        registerNativeModules();
    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
