package util;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * {@link Debugger}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Debugger {

    private Debugger() {
    }

    private static final Logger LOGGER = Logger.getGlobal();

    public static void info(@NotNull String info) {
        LOGGER.info(info);
    }

    public static void warning(@NotNull String warning) {
        LOGGER.warning(warning);
    }

    public static void exception(@NotNull Exception e) {
        LOGGER.warning(e.toString());
    }
}
