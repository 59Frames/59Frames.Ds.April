import management.ModuleManagement;
import model.interfaceable.Interactable;
import module.Module;
import module.speech.language.SpellCorrector;

import java.util.ServiceLoader;

public class April {

    public static void main(String[] args) {
        System.out.println(SpellCorrector.check("youu hav faund me"));

    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
