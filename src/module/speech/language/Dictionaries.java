package module.speech.language;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.Debugger.warning;

/**
 * {@link Dictionaries}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Dictionaries {
    private static Dictionary EN = null;
    private static Dictionary DE = null;
    private static Dictionary SHERLOCK = null;

    public static synchronized Dictionary english() {
        if (EN == null)
            EN = loadDictionary("en");
        return EN;
    }

    public static synchronized Dictionary german() {
        if (DE == null)
            DE = loadDictionary("de");
        return DE;
    }

    public static synchronized Dictionary sherlock() {
        if (SHERLOCK == null)
            SHERLOCK = loadDictionary("sherlock");
        return SHERLOCK;
    }

    @NotNull
    @Contract("_ -> new")
    private static Dictionary loadDictionary(@NotNull final String dictionary) {
        File file = FileUtil.load(String.format("dictionary/%s.txt", dictionary));

        HashMap<String, Integer> nWords = new HashMap<>();

        try {
            if (!file.exists()) throw new IOException("File Does not exist");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Pattern p = Pattern.compile("\\w+");
            for (String temp = ""; temp != null; temp = reader.readLine()) {
                temp = temp.strip();
                Matcher m = p.matcher(temp.toLowerCase());
                while (m.find()) nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
            }
            reader.close();
        } catch (Exception e) {
            warning(String.format("Unable to load dictionary {%s}", dictionary));
            e.printStackTrace();
        }

        return new Dictionary(nWords);
    }
}
