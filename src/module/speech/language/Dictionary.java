package module.speech.language;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link Dictionary}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Dictionary {
    private final Map<String, Integer> nWords = new HashMap<>();

    Dictionary(Map<String, Integer> words) {
        this.nWords.putAll(words);
    }

    int wordCount() {
        return nWords.size();
    }

    boolean containsWord(String input) {
        input = input.trim();
        return nWords.containsKey(input);
    }

    Map<String, Integer> getMap() {
        return nWords;
    }

    void add(String word) {
        if (containsWord(word)) {
            this.nWords.put(word, this.nWords.get(word) + 1);
        } else {
            this.nWords.put(word, 1);
        }
    }
}
