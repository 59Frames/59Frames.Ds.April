package environment;

import org.jetbrains.annotations.NotNull;
import util.Debugger;
import util.FileUtil;
import util.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * {@link Environment}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Environment {

    private static final Properties PROPERTIES = new Properties();

    private static boolean loaded = false;

    static {
        load();
    }

    public static void load() {
        if (loaded) return;

        try {
            PROPERTIES.load(new FileInputStream(FileUtil.load("config/app.properties")));
            loaded = true;
        } catch (IOException e) {
            Debugger.exception(e);
            loaded = false;
        }
    }

    public static String get(@NotNull final String key) {
        return PROPERTIES.getProperty(key, "");
    }

    public static boolean getBoolean(@NotNull final String key) {
        final String val = PROPERTIES.getProperty(key, "false");

        return Validator.isBool(val) && Boolean.parseBoolean(val);
    }

    public static double getDouble(@NotNull final String key) {
        final String val = PROPERTIES.getProperty(key, "-1.0");

        return Validator.isNumber(val)
                ? Double.parseDouble(val)
                : -1.0;
    }

    public static int getInteger(@NotNull final String key) {
        final String val = PROPERTIES.getProperty(key, "-1");

        return Validator.isNumber(val)
                ? Integer.parseInt(val)
                : -1;
    }
}
