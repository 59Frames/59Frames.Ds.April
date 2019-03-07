package module.bootstrap;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import util.Debugger;
import util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.prefs.Preferences;

/**
 * {@link Configuration}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Configuration {
    public static final String SECTION_APRIL = "APRIL";
    public static final String SECTION_BOOTSTRAP = "BOOTSTRAP";
    public static final String SECTION_EMOTION = "EMOTION";
    public static final String SECTION_KNOWLEDGE = "KNOWLEDGE";
    public static final String SECTION_MOTORIUM = "MOTORIUM";
    public static final String SECTION_SENSORIUM = "SENSORIUM";
    public static final String SECTION_SPEECH = "SPEECH";
    public static final String SECTION_VOLITION = "VOLITION";

    private static Preferences iniPrefs = null;

    public static void load() {
        if (iniPrefs != null)
            return;

        try {
            final Ini aprilIni = new Ini(FileUtil.load("config/april.ini"));
            iniPrefs = new IniPreferences(aprilIni);
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
}
