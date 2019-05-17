package model.exception;

/**
 * {@link MissingAnnotationException}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class MissingAnnotationException extends RuntimeException {

    public MissingAnnotationException() {
    }

    public MissingAnnotationException(String message) {
        super(message);
    }
}
