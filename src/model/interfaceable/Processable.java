package model.interfaceable;

/**
 * Processable
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Processable<T> {
    T process() throws Exception;
}
