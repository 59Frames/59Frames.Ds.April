package model.interfaceable;

/**
 * Startable is a functional interface just to simplify
 * programming whenever a class needs to be "started"
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface Startable {
    void start();
}
