package module.bootstrap;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
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
    private static final String SECTION_APRIL = "APRIL";
    private static final String SECTION_BOOTSTRAP = "BOOTSTRAP";
    private static final String SECTION_EMOTION = "EMOTION";
    private static final String SECTION_KNOWLEDGE = "KNOWLEDGE";
    private static final String SECTION_MOTORIUM = "MOTORIUM";
    private static final String SECTION_SENSORIUM = "SENSORIUM";
    private static final String SECTION_SPEECH = "SPEECH";
    private static final String SECTION_VOLITION = "VOLITION";

    public static void load() {
        try {
            final Ini aprilIni = new Ini(FileUtil.load("config/april.ini"));
            Preferences prefs = new IniPreferences(aprilIni);
            System.out.println(prefs.node(SECTION_APRIL).get("NAME", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
