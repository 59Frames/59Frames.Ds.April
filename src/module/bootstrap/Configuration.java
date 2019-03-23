package module.bootstrap;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.Debugger;
import util.FileUtil;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * {@link Configuration}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Configuration {

    private static Section april     = null;
    private static Section bootstrap = null;
    private static Section emotion   = null;
    private static Section knowledge = null;
    private static Section motorium  = null;
    private static Section sensorium = null;
    private static Section speech    = null;
    private static Section volition  = null;

    static {
        load();
    }

    private static void load() {
        try {
            final Preferences iniPrefs = new IniPreferences(new Ini(FileUtil.load("config/april.ini")));

            april = new Section(iniPrefs.node("APRIL"));
            bootstrap = new Section(iniPrefs.node("BOOTSTRAP"));
            emotion = new Section(iniPrefs.node("EMOTION"));
            knowledge = new Section(iniPrefs.node("KNOWLEDGE"));
            motorium = new Section(iniPrefs.node("MOTORIUM"));
            sensorium = new Section(iniPrefs.node("SENSORIUM"));
            speech = new Section(iniPrefs.node("SPEECH"));
            volition = new Section(iniPrefs.node("VOLITION"));
        } catch (IOException e) {
            Debugger.exception(e);
        }
    }

    @Contract(pure = true)
    public static synchronized Section april() {
        return april;
    }

    @Contract(pure = true)
    public static synchronized Section bootstrap() {
        return bootstrap;
    }

    @Contract(pure = true)
    public static synchronized Section emotion() {
        return emotion;
    }

    @Contract(pure = true)
    public static synchronized Section knowledge() {
        return knowledge;
    }

    @Contract(pure = true)
    public static synchronized Section motorium() {
        return motorium;
    }

    @Contract(pure = true)
    public static synchronized Section sensorium() {
        return sensorium;
    }

    @Contract(pure = true)
    public static synchronized Section speech() {
        return speech;
    }

    @Contract(pure = true)
    public static synchronized Section volition() {
        return volition;
    }

    public static class Section {
        private final Preferences section;

        private Section(@NotNull final Preferences preferences) {
            this.section = preferences;
        }

        public String get(@NotNull final String key) {
            return section.get(key.toUpperCase(), "");
        }
    }
}
