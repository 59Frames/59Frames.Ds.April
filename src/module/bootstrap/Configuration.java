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

    private static boolean loaded = false;

    private static Section april     = null;
    private static Section bootstrap = null;
    private static Section emotion   = null;
    private static Section knowledge = null;
    private static Section motorium  = null;
    private static Section sensorium = null;
    private static Section speech    = null;
    private static Section volition  = null;

    public static void load() {
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

            loaded = true;
        } catch (IOException e) {
            Debugger.exception(e);
            loaded = false;
        }
    }

    @Contract(pure = true)
    public static synchronized Section april() {
        if (!loaded)
            load();
        return april;
    }

    @Contract(pure = true)
    public static synchronized Section bootstrap() {
        if (!loaded)
            load();
        return bootstrap;
    }

    @Contract(pure = true)
    public static synchronized Section emotion() {
        if (!loaded)
            load();
        return emotion;
    }

    @Contract(pure = true)
    public static synchronized Section knowledge() {
        if (!loaded)
            load();
        return knowledge;
    }

    @Contract(pure = true)
    public static synchronized Section motorium() {
        if (!loaded)
            load();
        return motorium;
    }

    @Contract(pure = true)
    public static synchronized Section sensorium() {
        if (!loaded)
            load();
        return sensorium;
    }

    @Contract(pure = true)
    public static synchronized Section speech() {
        if (!loaded)
            load();
        return speech;
    }

    @Contract(pure = true)
    public static synchronized Section volition() {
        if (!loaded)
            load();
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

        public boolean getBoolean(@NotNull final String key) {
            return section.getBoolean(key, false);
        }

        public int getInteger(@NotNull final String key) {
            return section.getInt(key, 0);
        }

        public double getDouble(@NotNull final String key) {
            return section.getDouble(key, 0d);
        }
    }
}
