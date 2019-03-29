package model.interfaceable;

/**
 * Order is a functional interface just to simplify
 * to create an order which needs to be executed.
 * Common usage:
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface Order {
    void execute();
}
