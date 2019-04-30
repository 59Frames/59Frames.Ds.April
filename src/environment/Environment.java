package environment;

import org.jetbrains.annotations.NotNull;
import util.Debugger;
import util.FileUtil;
import util.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * {@link Environment}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Environment {

    private static final HashMap<String, Properties> PROPERTIES = new HashMap<>();
    private static final String[] PROPERTIES_FILE_NAMES = {"april", "bootstrap", "emotion", "knowledge", "motorium", "sensorium", "speech", "volition"};

    private static boolean loaded = false;

    static {
        load();
    }

    public static void load() {
        if (loaded) return;

        try {
            Properties tmpProps;
            for (String key : PROPERTIES_FILE_NAMES) {
                tmpProps = new Properties();
                tmpProps.load(new FileInputStream(FileUtil.load(String.format("config/%s.properties", key))));
                PROPERTIES.put(key.toLowerCase(), tmpProps);
            }
            loaded = true;
        } catch (IOException e) {
            Debugger.exception(e);
            loaded = false;
        }
    }

    public static String get(@NotNull final String key, @NotNull final String defaultValue) {
        final String[] result = {defaultValue};
        PROPERTIES.forEach((propKey, prop) -> {
            if (prop.containsKey(key)) {
                result[0] = prop.getProperty(key, defaultValue);
            }
        });
        return result[0];
    }

    public static String get(@NotNull final String key) {
        return get(key, "");
    }

    public static boolean getBoolean(@NotNull final String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(@NotNull final String key, final boolean defaultValue) {
        final String val = get(key, String.valueOf(defaultValue));
        return Validator.isBool(val) && Boolean.parseBoolean(val);
    }

    public static double getDouble(@NotNull final String key) {
        return getDouble(key, -1.0);
    }

    public static double getDouble(@NotNull final String key, final double defaultValue) {
        final String val = get(key, String.valueOf(defaultValue));

        return Validator.isNumber(val)
                ? Double.parseDouble(val)
                : -1.0;
    }

    public static int getInteger(@NotNull final String key) {
        return getInteger(key, -1);
    }

    public static int getInteger(@NotNull final String key, final int defaultValue) {
        final String val = get(key, String.valueOf(defaultValue));

        return Validator.isNumber(val)
                ? Integer.parseInt(val)
                : -1;
    }
}
