package module.speech.language;

import org.jetbrains.annotations.NotNull;
import util.StringUtil;
import util.Validator;

import java.util.*;

/**
 * {@link SpellCorrector}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class SpellCorrector {
    private static SpellCorrector instance = null;

    private final Dictionary dictionary;

    private SpellCorrector(Dictionary dictionary) {
        this.dictionary = dictionary;
        train(dictionary);
    }

    public static synchronized SpellCorrector getInstance() {
        if (instance == null)
            load(Dictionaries.english());
        return instance;
    }

    public static void load(Dictionary dictionary) {
        instance = new SpellCorrector(dictionary);
    }

    public static Dictionary getCurrentDictionary() {
        return getInstance().dictionary;
    }

    @NotNull
    public String check(String input) {
        return validate(input);
    }

    @NotNull
    private String validate(String input) {
        input = StringUtil.clean(input);

        String word;
        Scanner spellChecker = new Scanner(input);
        spellChecker.useDelimiter("\\s");
        StringBuilder builder = new StringBuilder();

        while (spellChecker.hasNextLine()) {
            word = spellChecker.next().strip();
            builder.append(" ");
            if (!isSpelledCorrectly(word)) {
                builder.append(correct(word));
            } else {
                builder.append(word);
            }
        }

        return builder.toString().strip();
    }

    private String correct(String word) {
        if (dictionary.containsWord(word)) return word;
        ArrayList<String> list = edits(word);
        HashMap<Integer, String> candidates = new HashMap<>();
        for (String s : list) if (this.dictionary.containsWord(s)) candidates.put(this.dictionary.getOccurrences(s), s);
        if (candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
        for (String s : list)
            for (String w : edits(s)) if (this.dictionary.containsWord(w)) candidates.put(this.dictionary.getOccurrences(w), w);
        return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
    }

    private void train(@NotNull Dictionary dictionary) {
        dictionary.getMap().forEach((word, group) -> {
            StringTokenizer tok = new StringTokenizer(word);
            while (tok.hasMoreTokens()) {
                this.trainSingle(tok.nextToken());
            }
        });
    }

    private void trainSingle(@NotNull String word) {
        String key = word.strip();
        this.dictionary.addOrUpdate(key);
    }

    private ArrayList<String> edits(@NotNull String word) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i + 1));
        for (int i = 0; i < word.length() - 1; ++i)
            result.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1) + word.substring(i + 2));
        for (int i = 0; i < word.length(); ++i)
            for (char c = 'a'; c <= 'z'; ++c) result.add(word.substring(0, i) + c + word.substring(i + 1));
        for (int i = 0; i <= word.length(); ++i)
            for (char c = 'a'; c <= 'z'; ++c) result.add(word.substring(0, i) + c + word.substring(i));
        return result;
    }

    private boolean isSpelledCorrectly(String input) {
        if (!Validator.isSpecialCharacter(input) && !Validator.isNumber(input))
            return dictionary.containsWord(input) || Dictionaries.english().containsWord(input);
        return true;
    }
}
