import management.ModuleManagement;
import model.interfaceable.Interactable;
import module.Module;
import module.speech.language.Dictionaries;
import module.speech.language.SpellCorrector;

public class April {

    public static void main(String[] args) {
        var corrector = SpellCorrector.load(Dictionaries.german());

        System.out.println(corrector.check("duuu hst nich gefundn"));

    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
