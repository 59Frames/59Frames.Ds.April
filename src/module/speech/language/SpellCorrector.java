package module.speech.language;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.StringUtil;
import util.ValidationUtil;

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

    public static synchronized SpellCorrector getInstance() {
        if (instance == null)
            instance = new SpellCorrector();
        return instance;
    }

    private Map<String, Integer> nWords = new HashMap<>(1);

    private SpellCorrector() {
        train(Dictionaries.english());
        train(Dictionaries.sherlock());
    }

    @NotNull
    public static String check(String input) {
        return getInstance().validate(input);
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
        if (nWords.containsKey(word)) return word;
        ArrayList<String> list = edits(word);
        HashMap<Integer, String> candidates = new HashMap<>();
        for (String s : list) if (this.nWords.containsKey(s)) candidates.put(this.nWords.get(s), s);
        if (candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
        for (String s : list)
            for (String w : edits(s)) if (this.nWords.containsKey(w)) candidates.put(this.nWords.get(w), w);
        return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
    }

    private void train(@NotNull Dictionary dictionary) {
        dictionary.getMap().forEach((word, group) -> {
            StringTokenizer tok = new StringTokenizer(word);
            while(tok.hasMoreTokens()) {
                this.trainSingle(tok.nextToken());
            }
        });
    }

    @NotNull
    @Contract("_, _ -> new")
    public final Dictionary mergeDictionaries(@NotNull Dictionary a, @NotNull Dictionary b) {
        return new Dictionary(mergeMaps(a.getMap(), b.getMap()));
    }

    private Map<String, Integer> mergeMaps(@NotNull Map<String, Integer> map1, Map<String, Integer> map2) {
        Map<String, Integer> result = new HashMap<>();
        Iterator iter = map1.keySet().iterator();

        String key;
        while(iter.hasNext()) {
            key = (String)iter.next();
            if (map2.containsKey(key)) {
                result.put(key, map1.get(key) + map2.get(key));
            } else {
                result.put(key, map1.get(key));
            }
        }

        iter = map2.keySet().iterator();

        while(iter.hasNext()) {
            key = (String)iter.next();
            if (!map1.containsKey(key)) {
                result.put(key, map2.get(key));
            }
        }

        return result;
    }

    private void trainSingle(@NotNull String word) {
        String key = word.strip().toLowerCase();
        if (this.nWords.containsKey(key)) {
            this.nWords.put(key, this.nWords.get(key) + 1);
        } else {
            this.nWords.put(key, 1);
        }
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
        if (!ValidationUtil.isSpecialCharacter(input) && !ValidationUtil.isNumber(input))
            return nWords.containsKey(input) || Dictionaries.english().containsWord(input);
        return true;
    }
}
