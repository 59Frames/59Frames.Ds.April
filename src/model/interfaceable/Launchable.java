package model.interfaceable;

/**
 * Launchable is a functional interface just to simplify
 * programming whenever a class needs to be "launched"
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface Launchable {
    void launch();
}
