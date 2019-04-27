package module.speech.language;

import org.jetbrains.annotations.NotNull;

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
    private final String name;
    private final Map<String, Integer> nWords = new HashMap<>(1);

    public Dictionary(@NotNull final String name, @NotNull final Map<String, Integer> words) {
        this.name = name;
        this.nWords.putAll(words);
    }

    public int wordCount() {
        return nWords.size();
    }

    public boolean containsWord(@NotNull final String key) {
        return nWords.containsKey(key.strip());
    }

    public int getOccurrences(@NotNull final String key) {
        return containsWord(key)
                ? nWords.get(key)
                :0;
    }

    public Map<String, Integer> getMap() {
        return nWords;
    }

    public void addOrUpdate(@NotNull final String word) {
        if (containsWord(word)) {
            this.nWords.put(word, this.nWords.get(word) + 1);
        } else {
            this.nWords.put(word, 1);
        }
    }

    public String getName() {
        return this.name;
    }
}
