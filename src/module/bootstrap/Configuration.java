package module.bootstrap;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
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

    private static Preferences iniPrefs = null;

    public static Section april     = null;
    public static Section bootstrap = null;
    public static Section emotion   = null;
    public static Section knowledge = null;
    public static Section motorium  = null;
    public static Section sensorium = null;
    public static Section speech    = null;
    public static Section volition  = null;

    public static void load() {
        if (iniPrefs != null)
            return;

        try {
            final Ini aprilIni = new Ini(FileUtil.load("config/april.ini"));
            iniPrefs = new IniPreferences(aprilIni);

            april = new Section(iniPrefs.node("APRIL"));
        } catch (IOException e) {
            Debugger.exception(e);
        }
    }

    public static String get(String section, String key) {
        checkIfWasLoaded();
        return iniPrefs.node(section).get(key, "");
    }

    private static void checkIfWasLoaded() {
        if (iniPrefs == null)
            load();
    }

    public static class Section {
        private final Preferences section;

        private Section(Preferences preferences) {
            this.section = preferences;
        }

        public String get(String key) {
            return section.get(key, "");
        }
    }
}
