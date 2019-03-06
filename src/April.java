import module.speech.SpellCorrector;

public class April {
    public static void main(String[] args) {
        var sentence = "Sometimze ithink about lifee itsell and i wonder how thiz workt";

        System.out.println(SpellCorrector.check(sentence));
    }
}
