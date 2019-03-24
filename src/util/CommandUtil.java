package util;

import _59frames.ds.lando.CommandListener;
import _59frames.ds.lando.model.Command;

/**
 * {@link CommandUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class CommandUtil {
    private static CommandListener listener = null;

    public static synchronized void registerListener(CommandListener newListener) {
        listener = newListener;
    }

    public static void add(Command command) {
        if (listener != null)
            listener.add(command);
    }

    public static void stop() {
        if (listener != null)
            listener.stop();
    }

    public static void start() {
        if (listener != null)
            listener.start();
    }

    public static boolean isRunning() {
        return listener != null && listener.isRunning();
    }
}
