package module.bootstrap.environment;

import org.jetbrains.annotations.NotNull;

import java.util.prefs.Preferences;

/**
 * {@link Section}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Section {
    private final Preferences section;

    public Section(@NotNull final Preferences preferences) {
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
