package util;

import _59frames.ds.lando.CommandListener;
import _59frames.ds.lando.model.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link CommandUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class CommandUtil {
    private static CommandListener listener = null;

    public static synchronized void registerListener(@NotNull final CommandListener newListener) {
        listener = newListener;
    }

    public static void add(@NotNull final Command command) {
        if (listener != null)
            listener.add(command);
    }

    public static void add(@NotNull final List<Command> commands) {
        if (listener != null)
            if (!commands.isEmpty())
                for (var c : commands) add(c);
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
