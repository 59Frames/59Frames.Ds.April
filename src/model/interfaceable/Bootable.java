package model.interfaceable;

/**
 * Bootable is a functional interface just to simplify
 * programming whenever a class needs to be "booted"
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface Bootable {
    void boot();
}
